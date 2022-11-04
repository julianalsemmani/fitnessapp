package com.groupfive.fitnessapp.model.calendar.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.model.calendar.PlannedWorkoutSession
import kotlinx.coroutines.tasks.await
import java.time.Instant

class FirebaseCalendarRepository : CalendarRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override suspend fun createPlannedWorkoutSession(
        startTime: Instant,
        endTime: Instant
    ) {
        if(auth.currentUser == null) return

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

    override suspend fun updatePlannedWorkoutSession(
        id: String,
        startTime: Instant,
        endTime: Instant
    ) {
        if(auth.currentUser == null) return

        val plannedWorkoutSession = mapOf(
            "startTime" to startTime.toEpochMilli(),
            "endTime" to endTime.toEpochMilli(),
        )

        userCalendarCollection()
            .document(id)
            .update(plannedWorkoutSession)
            .await()
    }

    override suspend fun deletePlannedWorkoutSession(id: String) {
        if(auth.currentUser == null) return

        userCalendarCollection()
            .document(id)
            .delete()
            .addOnFailureListener { exception ->
                Log.e(javaClass.simpleName, "failed to delete workout session", exception)
            }
            .await()
    }

    override suspend fun getPlannedWorkoutSession(id: String): PlannedWorkoutSession? {
        if(auth.currentUser == null) return null

        val document = userCalendarCollection()
            .document(id)
            .get()
            .await()

        if(document.data?.get("startTime") != null && document.data?.get("startTime") != null) {
            return PlannedWorkoutSession(
                document.id,
                Instant.ofEpochMilli(document.data!!["startTime"] as Long),
                Instant.ofEpochMilli(document.data!!["endTime"] as Long)
            )
        }
        return null
    }

    override suspend fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        if(auth.currentUser == null) return listOf()

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