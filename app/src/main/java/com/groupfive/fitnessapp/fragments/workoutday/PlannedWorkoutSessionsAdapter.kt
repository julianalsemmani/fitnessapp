package com.groupfive.fitnessapp.fragments.workoutday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.calendar.repository.PlannedWorkoutSession
import com.groupfive.fitnessapp.databinding.LayoutWorkoutSessionBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PlannedWorkoutSessionsAdapter(private val workoutDayViewModel: WorkoutDayViewModel)
    : RecyclerView.Adapter<PlannedWorkoutSessionsAdapter.ViewHolder>() {

    // Called when there's a need for a new ViewHolder (a new item in the list/grid)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_workout_session, parent, false)

        // Create the view holder with the corresponding view (list item)
        return ViewHolder(itemView)
    }

    // Called when data is bound to a specific ViewHolder (and item in the list/grid)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutSessionAtPosition = workoutDayViewModel.plannedWorkoutSessions.value?.get(position)!!

        holder.bind(workoutSessionAtPosition)
    }

    override fun getItemCount(): Int {
        return workoutDayViewModel.plannedWorkoutSessions.value?.size ?: 0
    }


    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val binding = LayoutWorkoutSessionBinding.bind(view)

        fun bind(item: PlannedWorkoutSession) {
            val startTime = LocalDateTime.ofInstant(item.startTime, ZoneId.systemDefault())
            binding.beginTimeView.text = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val endTime = LocalDateTime.ofInstant(item.endTime, ZoneId.systemDefault())
            binding.endTimeView.text = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            binding.workoutTypeView.text = item.workoutType.name
        }
    }
}