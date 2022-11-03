package com.groupfive.fitnessapp.screens.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.calendar.repository.FirebaseCalendarRepository
import com.groupfive.fitnessapp.calendar.repository.PlannedWorkoutSession
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class SetupPlannedExerciseViewModel: ViewModel() {
    private val calendarRepository = FirebaseCalendarRepository()

    private val _day = MutableLiveData<LocalDate>()
    val day: LiveData<LocalDate>
        get() = _day

    private val _startTime = MutableLiveData<Instant>()
    val startTime: LiveData<Instant>
        get() = _startTime

    private val _durationMinutes = MutableLiveData(30)
    val durationMinutes: LiveData<Int>
        get() = _durationMinutes

    private var plannedWorkoutSessionId: String? = null

    fun setDay(day: LocalDate) {
        _day.value = day
    }

    fun setStartTime(startTime: Instant) {
        _startTime.value = startTime
    }

    fun setDurationMinutes(durationMinutes: Int) {
        _durationMinutes.value = durationMinutes
    }

    fun setPlannedWorkoutSession(plannedWorkoutSession: PlannedWorkoutSession) {
        setStartTime(plannedWorkoutSession.startTime)
        setDurationMinutes(Duration.between(plannedWorkoutSession.startTime, plannedWorkoutSession.endTime).toMinutes().toInt())
        plannedWorkoutSessionId = plannedWorkoutSession.id
    }

    fun submitPlannedWorkoutSession() {
        if(startTime.value != null) {
            //TODO(Edward): Do we really need to run blocking all the time here?
            runBlocking {
                calendarRepository.createPlannedWorkoutSession(
                    startTime.value!!,
                    startTime.value!!.plusSeconds((durationMinutes.value!!*60).toLong())
                )
            }
        }
    }

}