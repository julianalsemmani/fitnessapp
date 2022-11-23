package com.groupfive.fitnessapp.screens.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.model.plannedworkout.repository.FirebasePlannedWorkoutRepository
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import com.groupfive.fitnessapp.model.workout.repository.FirebaseWorkoutSessionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset

class CalendarViewModel: ViewModel() {

    private val plannedWorkoutRepository = FirebasePlannedWorkoutRepository()
    private val workoutRepository = FirebaseWorkoutSessionRepository()

    private val _plannedWorkoutSessions = MutableLiveData<List<PlannedWorkoutSession>>()
    val plannedWorkoutSessions: LiveData<List<PlannedWorkoutSession>>
        get() = _plannedWorkoutSessions

    private val _workoutSessions = MutableLiveData<List<WorkoutSession>>()
    val workoutSessions: LiveData<List<WorkoutSession>>
        get() = _workoutSessions

    private val _currentMonth = MutableLiveData(YearMonth.now())
    val currentMonth: LiveData<YearMonth>
        get() = _currentMonth

    init {
        runBlocking {
            val plannedWorkoutSessions = async { plannedWorkoutRepository.getPlannedWorkoutSessions() }
            val workoutSessions = async { workoutRepository.getWorkoutSessions() }
            
            _plannedWorkoutSessions.value = plannedWorkoutSessions.await()
            _workoutSessions.value = workoutSessions.await()
        }
    }

    fun setCurrentMonth(currentMonth: YearMonth) {
        _currentMonth.value = currentMonth
    }
}