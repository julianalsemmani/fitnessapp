package com.groupfive.fitnessapp.screens.calendar

import android.view.View
import com.groupfive.fitnessapp.databinding.LayoutCalendarMonthHeaderBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    private val binding = LayoutCalendarMonthHeaderBinding.bind(view)

    val textView = binding.headerTextView
    val dayTexts = listOf(binding.day1Text,
        binding.day2Text,
        binding.day3Text,
        binding.day4Text,
        binding.day5Text,
        binding.day6Text,
        binding.day7Text)

}