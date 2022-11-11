package com.groupfive.fitnessapp.model.plannedworkout.repository

import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import kotlinx.coroutines.runBlocking
import java.time.Instant
import kotlin.math.roundToLong
import kotlin.random.Random

class TestPlannedWorkoutRepository: PlannedWorkoutRepository {
    private var plannedWorkoutSessions: ArrayList<PlannedWorkoutSession> = ArrayList()
    private var nextId = 0

    override suspend fun createPlannedWorkoutSession(startTime: Instant, endTime: Instant) {
        plannedWorkoutSessions.add(PlannedWorkoutSession((nextId++).toString(), startTime, endTime))
    }

    override suspend fun updatePlannedWorkoutSession(
        id: String,
        startTime: Instant,
        endTime: Instant
    ) {
        deletePlannedWorkoutSession(id)
        createPlannedWorkoutSession(startTime, endTime)
    }

    override suspend fun deletePlannedWorkoutSession(id: String) {
        plannedWorkoutSessions.removeAll { it.id == id }
    }

    override suspend fun getPlannedWorkoutSession(id: String): PlannedWorkoutSession? {
        return plannedWorkoutSessions.find { it.id == id }
    }

    override suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        return plannedWorkoutSessions
    }

    companion object {
        private var instance: TestPlannedWorkoutRepository? = null

        private suspend fun repositoryWithPlannedExercisesForToday(): TestPlannedWorkoutRepository {
            val result = TestPlannedWorkoutRepository()

            // Add random workouts with random time beginning from 5 minutes from now
            var time = Instant.now().plusSeconds(300)
            for (i in 1..20) {

                val endTime = time.plusSeconds((Random.nextFloat()*1800).roundToLong())

                result.createPlannedWorkoutSession(time, endTime)

                time =  endTime.plusSeconds((Random.nextFloat()*1800).roundToLong())
            }

            return result
        }

        fun instance(): TestPlannedWorkoutRepository {
            if(instance == null) {
                runBlocking {
                    instance = repositoryWithPlannedExercisesForToday()
                }
            }
            return instance as TestPlannedWorkoutRepository
        }
    }
}