package com.groupfive.fitnessapp.model.workout.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.exercise.WorkoutType
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import kotlinx.coroutines.tasks.await
import java.time.Instant

class FirebaseWorkoutSessionRepository : WorkoutSessionRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override suspend fun createWorkoutSession(
        startTime: Instant,
        endTime: Instant,
        reps: Int,
        workoutType: WorkoutType
    ) {
        val workoutSession = hashMapOf(
            "startTime" to startTime.toEpochMilli(),
            "endTime" to endTime.toEpochMilli(),
            "reps" to reps,
            "workoutType" to workoutType.name
        )

        userWorkoutCollection()
            .add(workoutSession)
            .addOnFailureListener { exception ->
                Log.e(javaClass.simpleName, "failed to add workout session", exception)
            }
            .await()
    }

    override suspend fun getWorkoutSession(id: String): WorkoutSession? {
        val workoutDocument = userWorkoutCollection()
            .document(id)
            .get()
            .await()

        return workoutDocument.data?.let { parseWorkoutSessionFromDocumentData(workoutDocument.id, it) }
    }

    override suspend fun getWorkoutSessions(): List<WorkoutSession> {
        val workoutCollection = userWorkoutCollection()
            .get()
            .await()

        return workoutCollection.documents.mapNotNull { workoutDocument ->
            workoutDocument.data?.let { parseWorkoutSessionFromDocumentData(workoutDocument.id, it) }
        }
    }

    private fun parseWorkoutSessionFromDocumentData(id: String, data: Map<String, Any>): WorkoutSession {
        return WorkoutSession(
            id = id,
            startTime = Instant.ofEpochMilli(data["startTime"] as Long),
            endTime = Instant.ofEpochMilli(data["endTime"] as Long),
            reps = (data["reps"] as Long).toInt(),
            workoutType = WorkoutType.valueOf(data["workoutType"] as String)
        )
    }

    private fun userWorkoutCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid!!)
            .collection("workout")
}