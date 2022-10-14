package com.groupfive.fitnessapp.fragments.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.calendar.repository.PlannedWorkoutSession
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import java.time.LocalDate
import java.time.ZoneOffset

class WorkoutDayViewModel: ViewModel() {
    //TODO(edward): Swap this out with a repository that communicates with firebase
    private val calendarRepository = TestCalendarRepository.instance()

    private val _plannedWorkoutSessions = MutableLiveData<List<PlannedWorkoutSession>>()
    val plannedWorkoutSessions: LiveData<List<PlannedWorkoutSession>>
        get() = _plannedWorkoutSessions

    private val _day = MutableLiveData<LocalDate>()
    val day: LiveData<LocalDate>
        get() = _day

    init {
        setDay(LocalDate.now())
    }

    fun setDay(day: LocalDate) {
        _day.value = day

        // Only show planned workouts that begin in the set day
        val startOfDay = day.atStartOfDay(ZoneOffset.systemDefault()).toInstant()
        val endOfDay = day.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).toInstant()
        _plannedWorkoutSessions.value = calendarRepository.getPlannedWorkoutSessions().filter {
            it.startTime.isAfter(startOfDay) && it.startTime.isBefore(endOfDay)
        }
    }

}