package com.groupfive.fitnessapp.model.calendar

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

data class PlannedWorkoutSession(val id: String, val startTime: Instant, val endTime: Instant)
