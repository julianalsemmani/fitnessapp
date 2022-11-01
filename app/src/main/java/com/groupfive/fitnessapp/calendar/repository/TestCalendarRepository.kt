package com.groupfive.fitnessapp.calendar.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant
import kotlin.math.roundToLong
import kotlin.random.Random

class TestCalendarRepository: CalendarRepository {
    private var plannedWorkoutSessions: ArrayList<PlannedWorkoutSession> = ArrayList()
    private var nextId = 0

    override fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant, workoutType: WorkoutType) {
        plannedWorkoutSessions.add(PlannedWorkoutSession(nextId++, startTime, endTime, workoutType))
    }

    override fun deletePlannedWorkoutSession(id: Int) {
        plannedWorkoutSessions.removeAll { it.id == id }
    }

    override fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        return plannedWorkoutSessions
    }

    companion object {
        private var instance: TestCalendarRepository? = null

        fun repositoryWithPlannedExercisesForToday(): TestCalendarRepository {
            val result = TestCalendarRepository()

            // Add random workouts with random time beginning from 5 minutes from now
            var time = Instant.now().plusSeconds(300)
            for (i in 1..20) {

                val endTime = time.plusSeconds((Random.nextFloat()*1800).roundToLong())
                result.createPlannedWorkoutSession(
                        time,
                        endTime,
                        WorkoutType.values()[Random.nextInt(WorkoutType.values().size)]
                )

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