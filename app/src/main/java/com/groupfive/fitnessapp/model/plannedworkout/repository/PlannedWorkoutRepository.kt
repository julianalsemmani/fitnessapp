package com.groupfive.fitnessapp.model.plannedworkout.repository

import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import java.time.Instant

interface PlannedWorkoutRepository {
    suspend fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant)
    suspend fun updatePlannedWorkoutSession(id: String, startTime: Instant, endTime: Instant)
    suspend fun deletePlannedWorkoutSession(id: String)
    suspend fun getPlannedWorkoutSession(id: String): PlannedWorkoutSession?
    suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession>
}