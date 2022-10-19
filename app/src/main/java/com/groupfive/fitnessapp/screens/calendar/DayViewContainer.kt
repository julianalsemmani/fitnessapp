package com.groupfive.fitnessapp.screens.calendar

import android.view.View
import com.groupfive.fitnessapp.databinding.LayoutCalendarDayBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = LayoutCalendarDayBinding.bind(view).dayText


}