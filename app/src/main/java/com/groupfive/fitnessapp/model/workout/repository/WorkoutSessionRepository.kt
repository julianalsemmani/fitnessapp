package com.groupfive.fitnessapp.model.workout.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import java.time.Instant

interface WorkoutSessionRepository {
    suspend fun createWorkoutSession(startTime: Instant, endTime: Instant, reps: Int, workoutType: WorkoutType)
    suspend fun getWorkoutSession(id: String): WorkoutSession?
    suspend fun getWorkoutSessions(): List<WorkoutSession>
}