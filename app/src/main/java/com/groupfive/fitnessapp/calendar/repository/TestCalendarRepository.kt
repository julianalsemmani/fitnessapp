package com.groupfive.fitnessapp.calendar.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant
import kotlin.math.roundToLong
import kotlin.random.Random

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
        private var instance: TestCalendarRepository? = null

        fun repositoryWithPlannedExercisesForToday(): TestCalendarRepository {
            val result = TestCalendarRepository()

            // Add random workouts with random time beginning from now
            var time = Instant.now()
            for (i in 1..20) {

                val endTime = time.plusSeconds((Random.nextFloat()*1800).roundToLong())
                result.createPlannedWorkoutSession(PlannedWorkoutSession(
                    time,
                    endTime,
                    WorkoutType.values()[Random.nextInt(WorkoutType.values().size)]))

                time =  endTime.plusSeconds((Random.nextFloat()*1800).roundToLong())
            }

            return result
        }

        fun instance(): TestCalendarRepository {
            if(instance == null) {
                instance = repositoryWithPlannedExercisesForToday()
            }
            return instance as TestCalendarRepository
        }
    }
}