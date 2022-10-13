package com.groupfive.fitnessapp.calendar.repository

class TestCalendarRepository: CalendarRepository {
    private val plannedWorkoutSessions: ArrayList<PlannedWorkoutSession> = ArrayList()

    override fun createPlannedWorkoutSession(plannedWorkoutSession: PlannedWorkoutSession) {
        plannedWorkoutSessions.add(plannedWorkoutSession)
    }

    override fun deletePlannedWorkoutSession(plannedWorkoutSession: PlannedWorkoutSession) {
        plannedWorkoutSessions.remove(plannedWorkoutSession)
    }

    override fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        return plannedWorkoutSessions
    }
}