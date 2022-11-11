package com.groupfive.fitnessapp.screens.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.model.plannedworkout.repository.FirebasePlannedWorkoutRepository
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import com.groupfive.fitnessapp.model.workout.repository.FirebaseWorkoutSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneOffset

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
        val startOfDay = day.atStartOfDay(ZoneOffset.systemDefault()).toInstant()
        val endOfDay = day.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).toInstant()
        runBlocking {
            // Get planned workouts and completed workouts in parallel
            val plannedWorkouts = async {
                plannedWorkoutRepository.getPlannedWorkoutSessions().filter {
                    it.startTime.isAfter(startOfDay) && it.startTime.isBefore(endOfDay)
                }
            }

            val workoutSessions = async {
                workoutRepository.getWorkoutSessions().filter {
                    it.startTime.isAfter(startOfDay) && it.startTime.isBefore(endOfDay)
                }
            }

            _plannedWorkoutSessions.value = plannedWorkouts.await()
            _workoutSessions.value = workoutSessions.await()
        }


    }

}