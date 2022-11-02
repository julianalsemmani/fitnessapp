package com.groupfive.fitnessapp.user.repository

import com.groupfive.fitnessapp.calendar.repository.PlannedWorkoutSession
import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

interface UserRepository {
    suspend fun getLoggedInUser(): User
}