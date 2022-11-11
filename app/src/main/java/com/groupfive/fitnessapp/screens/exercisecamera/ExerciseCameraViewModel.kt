package com.groupfive.fitnessapp.screens.exercisecamera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.exercise.WorkoutType
import com.groupfive.fitnessapp.model.workout.repository.FirebaseWorkoutSessionRepository
import kotlinx.coroutines.runBlocking
import java.time.Instant

class ExerciseCameraViewModel: ViewModel() {
    private val workoutSessionRepository = FirebaseWorkoutSessionRepository()

    private val startTime = Instant.now()

    private val _workoutType = MutableLiveData<WorkoutType>()
    val workoutType: LiveData<WorkoutType>
        get() = _workoutType

    private val _reps = MutableLiveData<Int>(0)
    val reps: LiveData<Int>
        get() = _reps

    fun setWorkoutType(workoutType: WorkoutType) {
        _workoutType.value = workoutType
    }

    fun addRep() {
        _reps.value = _reps.value!! + 1
    }

    fun submitWorkoutSession() {
        if(_reps.value!! > 0 && workoutType.value != null) {
            runBlocking {
                workoutSessionRepository.createWorkoutSession(
                    startTime = startTime,
                    endTime = Instant.now()!!,
                    reps = reps.value!!,
                    workoutType = workoutType.value!!
                )

                _reps.value = 0
            }
        } else {
            Log.e(javaClass.simpleName, "Failed to submit workout session, invalid reps or workout type")
        }
    }

}