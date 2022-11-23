package com.groupfive.fitnessapp.screens.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.R
import com.groupfive.fitnessapp.databinding.FragmentCalendarBinding
import com.groupfive.fitnessapp.util.CalendarUtils
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.ScrollMode
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val calendarView = binding.calendarView

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.dayNumberView.text = day.date.dayOfMonth.toString()

                var backgroundColor = MaterialColors.getColor(view!!, R.attr.colorSurfaceVariant)
                var textColor = MaterialColors.getColor(view!!, R.attr.colorOnSurfaceVariant)

                // Color background green if there are completed  workout sessions in this day
                if(viewModel.workoutSessions.value?.any { CalendarUtils.isInstantInDay(it.startTime, day.date) } == true) {
                    backgroundColor = MaterialColors.getColor(view!!, R.attr.colorSecondary)
                    textColor = MaterialColors.getColor(view!!, R.attr.colorOnSecondary)
                }
                // Color background primary if there are planned workout sessions in this day
                else if(viewModel.plannedWorkoutSessions.value?.any { CalendarUtils.isInstantInDay(it.startTime, day.date) } == true) {
                    backgroundColor = MaterialColors.getColor(view!!, R.attr.colorPrimary)
                    textColor = MaterialColors.getColor(view!!, R.attr.colorOnPrimary)
                }

                // Color days that are outside selected month differently
                if (day.owner != DayOwner.THIS_MONTH) {
                    textColor = Color.GRAY
                }

                container.dayBackground.setCardBackgroundColor(backgroundColor)
                container.dayNumberView.setTextColor(textColor)

                container.view.setOnClickListener {
                    findNavController().navigate(
                        CalendarFragmentDirections.actionCalendarFragmentToWorkoutDayFragment(
                            day.date
                        )
                    )
                }
            }
        }

        val daysOfWeek = daysOfWeekFromLocale()
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = MonthViewContainer(view)

            // Called every time we need to reuse a container.
            @SuppressLint("SetTextI18n")
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.lowercase(Locale.getDefault()).replaceFirstChar { char -> char.uppercase() }} ${month.year}"
                container.dayTexts.forEachIndexed { i, textView -> textView.text = daysOfWeek[i].name.substring(0, 3) }

                container.previousMonthButton.setOnClickListener {
                    viewModel.setCurrentMonth(viewModel.currentMonth.value!!.minusMonths(1))
                }
                container.nextMonthButton.setOnClickListener {
                    viewModel.setCurrentMonth(viewModel.currentMonth.value!!.plusMonths(1))
                }
            }
        }

        // Update calendar view from current month in view model
        viewModel.currentMonth.observe(viewLifecycleOwner) {
            calendarView.scrollToMonth(it)
        }
        calendarView.monthScrollListener = {
            viewModel.setCurrentMonth(it.yearMonth)
        }

        calendarView.scrollMode = ScrollMode.PAGED
        calendarView.orientation = 0
        calendarView.maxRowCount = 6

        val currentMonth = viewModel.currentMonth.value!!
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        return binding.root
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek is not DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            return rhs + lhs
        }
        return daysOfWeek
    }

    companion object {

    }
}