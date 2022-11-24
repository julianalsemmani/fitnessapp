package com.groupfive.fitnessapp.screens.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import com.groupfive.fitnessapp.model.plannedworkout.repository.FirebasePlannedWorkoutRepository
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import com.groupfive.fitnessapp.model.workout.repository.FirebaseWorkoutSessionRepository
import kotlinx.coroutines.launch
import java.time.YearMonth

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
        viewModelScope.launch {
            launch {
                _plannedWorkoutSessions.value = plannedWorkoutRepository.getPlannedWorkoutSessions()
            }
            launch {
                _workoutSessions.value = workoutRepository.getWorkoutSessions()
            }
        }
    }

    fun setCurrentMonth(currentMonth: YearMonth) {
        _currentMonth.value = currentMonth
    }
}