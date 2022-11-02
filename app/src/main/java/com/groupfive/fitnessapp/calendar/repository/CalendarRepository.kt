package com.groupfive.fitnessapp.calendar.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

interface CalendarRepository {
    suspend fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant, workoutType: WorkoutType)
    suspend fun deletePlannedWorkoutSession(id: String)
    suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession>
}