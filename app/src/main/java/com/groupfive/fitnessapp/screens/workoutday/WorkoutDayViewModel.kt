package com.groupfive.fitnessapp.screens.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.model.calendar.repository.FirebaseCalendarRepository
import com.groupfive.fitnessapp.model.calendar.PlannedWorkoutSession
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneOffset

class WorkoutDayViewModel: ViewModel() {
    private val calendarRepository = FirebaseCalendarRepository()

    private val _plannedWorkoutSessions = MutableLiveData<List<PlannedWorkoutSession>>()
    val plannedWorkoutSessions: LiveData<List<PlannedWorkoutSession>>
        get() = _plannedWorkoutSessions

    private val _day = MutableLiveData<LocalDate>()
    val day: LiveData<LocalDate>
        get() = _day

    fun setDay(day: LocalDate) {
        _day.value = day

        // Only show planned workouts that begin in the set day
        val startOfDay = day.atStartOfDay(ZoneOffset.systemDefault()).toInstant()
        val endOfDay = day.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).toInstant()
        runBlocking {
            _plannedWorkoutSessions.value = calendarRepository.getPlannedWorkoutSessions().filter {
                it.startTime.isAfter(startOfDay) && it.startTime.isBefore(endOfDay)
            }
        }
    }

}