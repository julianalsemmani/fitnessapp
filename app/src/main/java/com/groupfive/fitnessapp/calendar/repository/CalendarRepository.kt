package com.groupfive.fitnessapp.calendar.repository

interface CalendarRepository {
    fun createPlannedWorkoutSession(plannedWorkoutSession: PlannedWorkoutSession)
    fun deletePlannedWorkoutSession(plannedWorkoutSession: PlannedWorkoutSession)
    fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession>
}