package com.groupfive.fitnessapp.model.calendar.repository

import com.groupfive.fitnessapp.model.calendar.PlannedWorkoutSession
import java.time.Instant

interface CalendarRepository {
    suspend fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant)
    suspend fun updatePlannedWorkoutSession(id: String, startTime: Instant, endTime: Instant)
    suspend fun deletePlannedWorkoutSession(id: String)
    suspend fun getPlannedWorkoutSession(id: String): PlannedWorkoutSession?
    suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession>
}