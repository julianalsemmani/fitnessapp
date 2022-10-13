package com.groupfive.fitnessapp.fragments.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.calendar.repository.PlannedWorkoutSession
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import java.time.Instant
import java.util.Date

class WorkoutDayViewModel: ViewModel() {
    //TODO(edward): Swap this out with a repository that communicates with firebase
    private val calendarRepository = TestCalendarRepository.repositoryWithPlannedExercisesForToday()

    private val _plannedWorkoutSessions = MutableLiveData<List<PlannedWorkoutSession>>()
    val plannedWorkoutSessions: LiveData<List<PlannedWorkoutSession>>
        get() = _plannedWorkoutSessions

    private val _day = MutableLiveData<Date>()
    val day: LiveData<Date>
        get() = _day

    init {
        _plannedWorkoutSessions.value = calendarRepository.getPlannedWorkoutSessions()
        _day.value = Date.from(Instant.now())
    }

}