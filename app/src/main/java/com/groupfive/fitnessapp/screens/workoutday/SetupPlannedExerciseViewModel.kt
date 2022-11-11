package com.groupfive.fitnessapp.screens.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.model.plannedworkout.repository.FirebasePlannedWorkoutRepository
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

class SetupPlannedExerciseViewModel: ViewModel() {
    private val plannedWorkoutRepository = FirebasePlannedWorkoutRepository()

    private val _day = MutableLiveData<LocalDate>()
    val day: LiveData<LocalDate>
        get() = _day

    private val _startTime = MutableLiveData<Instant>()
    val startTime: LiveData<Instant>
        get() = _startTime

    private val _durationMinutes = MutableLiveData(30)

    private var plannedWorkoutSessionToUpdateId: String? = null

    fun setDay(day: LocalDate) {
        _day.value = day
    }

    fun setStartTime(startTime: Instant) {
        _startTime.value = startTime
    }

    fun setDurationMinutes(durationMinutes: Int) {
        _durationMinutes.value = durationMinutes
    }

    fun getDurationMinutes(): Int {
        return _durationMinutes.value!!
    }

    fun setPlannedWorkoutSessionToUpdate(plannedWorkoutSessionId: String?) {
        plannedWorkoutSessionToUpdateId = plannedWorkoutSessionId

        if(plannedWorkoutSessionId != null) {
            runBlocking {
                val plannedWorkoutSession = plannedWorkoutRepository.getPlannedWorkoutSession(plannedWorkoutSessionId)

                if(plannedWorkoutSession != null) {
                    setStartTime(plannedWorkoutSession.startTime)
                    setDurationMinutes(Duration.between(plannedWorkoutSession.startTime, plannedWorkoutSession.endTime).toMinutes().toInt())
                    plannedWorkoutSessionToUpdateId = plannedWorkoutSessionId
                }
            }
        }
    }

    fun submitPlannedWorkoutSession() {
        if(startTime.value != null && getDurationMinutes() > 0) {
            if(plannedWorkoutSessionToUpdateId != null) {
                // Update session with given id
                //TODO(Edward): Do we really need to run blocking all the time here?
                runBlocking {
                    plannedWorkoutRepository.updatePlannedWorkoutSession(
                        plannedWorkoutSessionToUpdateId!!,
                        startTime.value!!,
                        startTime.value!!.plusSeconds((getDurationMinutes()*60).toLong())
                    )
                }
            } else {
                // Create new session
                //TODO(Edward): Do we really need to run blocking all the time here?
                runBlocking {
                    plannedWorkoutRepository.createPlannedWorkoutSession(
                        startTime.value!!,
                        startTime.value!!.plusSeconds((getDurationMinutes()*60).toLong())
                    )
                }
            }
        }
    }

}