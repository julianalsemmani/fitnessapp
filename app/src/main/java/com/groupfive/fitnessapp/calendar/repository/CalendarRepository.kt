package com.groupfive.fitnessapp.calendar.repository

import java.time.Instant

interface CalendarRepository {
    suspend fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant)
    suspend fun deletePlannedWorkoutSession(id: String)
    suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession>
}