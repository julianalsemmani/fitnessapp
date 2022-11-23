package com.groupfive.fitnessapp.screens.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import com.groupfive.fitnessapp.model.workout.repository.FirebaseWorkoutSessionRepository
import kotlinx.coroutines.runBlocking

class StatsViewModel: ViewModel() {
    private val workoutRepository = FirebaseWorkoutSessionRepository()

    private val _workoutSessions = MutableLiveData<List<WorkoutSession>>()
    val workoutSessions: LiveData<List<WorkoutSession>>
        get() = _workoutSessions

    init {
        runBlocking {
            _workoutSessions.value = workoutRepository.getWorkoutSessions()
        }
    }
}