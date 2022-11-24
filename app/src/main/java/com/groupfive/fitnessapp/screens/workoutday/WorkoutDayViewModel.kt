package com.groupfive.fitnessapp.screens.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import com.groupfive.fitnessapp.model.plannedworkout.repository.FirebasePlannedWorkoutRepository
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import com.groupfive.fitnessapp.model.workout.repository.FirebaseWorkoutSessionRepository
import com.groupfive.fitnessapp.util.CalendarUtils
import kotlinx.coroutines.launch
import java.time.LocalDate

class WorkoutDayViewModel: ViewModel() {
    private val plannedWorkoutRepository = FirebasePlannedWorkoutRepository()
    private val workoutRepository = FirebaseWorkoutSessionRepository()

    private val _plannedWorkoutSessions = MutableLiveData<List<PlannedWorkoutSession>>()
    val plannedWorkoutSessions: LiveData<List<PlannedWorkoutSession>>
        get() = _plannedWorkoutSessions

    private val _workoutSessions = MutableLiveData<List<WorkoutSession>>()
    val workoutSessions: LiveData<List<WorkoutSession>>
        get() = _workoutSessions

    private val _day = MutableLiveData<LocalDate>()
    val day: LiveData<LocalDate>
        get() = _day

    fun setDay(day: LocalDate) {
        _day.value = day

        // Only show planned workouts/workouts that begin in the set day
        viewModelScope.launch {
            // Get planned workouts and completed workouts in parallel
            launch {
                _plannedWorkoutSessions.value = plannedWorkoutRepository.getPlannedWorkoutSessions().filter {
                    CalendarUtils.isInstantInDay(it.startTime, day)
                }
            }

            launch {
                _workoutSessions.value = workoutRepository.getWorkoutSessions().filter {
                    CalendarUtils.isInstantInDay(it.startTime, day)
                }
            }
        }
    }
}