package com.groupfive.fitnessapp.calendar.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

interface CalendarRepository {
    fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant, workoutType: WorkoutType)
    fun deletePlannedWorkoutSession(id: Int)
    fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession>
}