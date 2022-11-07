package com.groupfive.fitnessapp.model.workout

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

data class WorkoutSession(val id: String, val startTime: Instant, val endTime: Instant, val reps: Int, val workoutType: WorkoutType)
