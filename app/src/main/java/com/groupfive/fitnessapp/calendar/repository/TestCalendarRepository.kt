package com.groupfive.fitnessapp.calendar.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

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

    companion object {
        fun repositoryWithPlannedExercisesForToday(): TestCalendarRepository {
            val result = TestCalendarRepository()

            result.createPlannedWorkoutSession(PlannedWorkoutSession(
                Instant.now(),
                Instant.now().plusSeconds(3600),
                WorkoutType.SQUAT))
            result.createPlannedWorkoutSession(PlannedWorkoutSession(
                Instant.now().plusSeconds(4000),
                Instant.now().plusSeconds(5000),
                WorkoutType.PUSH_UP))

            return result
        }
    }
}