package com.groupfive.fitnessapp.screens.calendar

import android.view.View
import com.groupfive.fitnessapp.databinding.LayoutCalendarDayBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val binding = LayoutCalendarDayBinding.bind(view)

    val dayNumberView = binding.dayNumberView
    val dayBackground = binding.dayBackground

}