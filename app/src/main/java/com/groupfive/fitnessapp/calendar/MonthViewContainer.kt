package com.groupfive.fitnessapp.calendar

import android.view.View
import com.groupfive.fitnessapp.databinding.CalendarMonthHeaderLayoutBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarMonthHeaderLayoutBinding.bind(view)

    val textView = binding.headerTextView
}