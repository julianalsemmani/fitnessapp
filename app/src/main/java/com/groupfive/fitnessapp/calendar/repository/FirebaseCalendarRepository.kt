package com.groupfive.fitnessapp.calendar.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.Instant

class FirebaseCalendarRepository : CalendarRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override suspend fun createPlannedWorkoutSession(
        startTime: Instant,
        endTime: Instant
    ) {
        val plannedWorkoutSession = hashMapOf(
            "startTime" to startTime.toEpochMilli(),
            "endTime" to endTime.toEpochMilli(),
        )

        userCalendarCollection()
            .add(plannedWorkoutSession)
            .addOnFailureListener { exception ->
                Log.e(javaClass.simpleName, "failed to add workout session", exception)
            }
            .await()
    }

    override suspend fun deletePlannedWorkoutSession(id: String) {
        userCalendarCollection()
            .document(id)
            .delete()
            .addOnFailureListener { exception ->
                Log.e(javaClass.simpleName, "failed to delete workout session", exception)
            }
            .await()
    }

    override suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        val plannedWorkoutSessions = userCalendarCollection()
            .get()
            .await()

        return plannedWorkoutSessions.map {
            PlannedWorkoutSession(
                it.id,
                Instant.ofEpochMilli(it.data["startTime"]!! as Long),
                Instant.ofEpochMilli(it.data["endTime"]!! as Long)
            )
        }
    }

    private fun userCalendarCollection() =
        db.collection("users")
        .document(auth.currentUser?.uid!!)
        .collection("calendar")
}