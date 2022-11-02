package com.groupfive.fitnessapp.calendar.repository

import com.groupfive.fitnessapp.exercise.WorkoutType
import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlin.math.roundToLong
import kotlin.random.Random

class TestCalendarRepository: CalendarRepository {
    private var plannedWorkoutSessions: ArrayList<PlannedWorkoutSession> = ArrayList()
    private var nextId = 0

    override suspend fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant, workoutType: WorkoutType) {
        plannedWorkoutSessions.add(PlannedWorkoutSession((nextId++).toString(), startTime, endTime, workoutType))
    }

    override suspend fun deletePlannedWorkoutSession(id: String) {
        plannedWorkoutSessions.removeAll { it.id == id }
    }

    override suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        return plannedWorkoutSessions
    }

    companion object {
        private var instance: TestCalendarRepository? = null

        private suspend fun repositoryWithPlannedExercisesForToday(): TestCalendarRepository {
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
                runBlocking {
                    instance = repositoryWithPlannedExercisesForToday()
                }
            }
            return instance as TestCalendarRepository
        }
    }
}