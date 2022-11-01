package com.groupfive.fitnessapp.calendar.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Instant

class FirebaseCalendarRepository : CalendarRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun createPlannedWorkoutSession(
        startTime: Instant,
        endTime: Instant,
        workoutType: WorkoutType
    ) {
        val plannedWorkoutSession = hashMapOf(
            "startTime" to startTime.epochSecond,
            "endTime" to endTime.epochSecond,
            "workoutType" to workoutType.ordinal //TODO(Edward): we will change this later such that workout type is not part of calendar
        )

        db.collection("users")
            .document(auth.currentUser?.uid!!)
            .collection("calendar")
            .add(plannedWorkoutSession)
            .addOnSuccessListener {
                Log.e(javaClass.simpleName, "ADDED WORKOUT SUCCESS")
            }
            .addOnFailureListener { exception ->
                Log.e(javaClass.simpleName, "ADDED WORKOUT FAIL", exception)
            }
    }

    override fun deletePlannedWorkoutSession(id: Int) {
//        db.collection("users")
//            .document(auth.currentUser?.uid!!)
//            .collection("calendar")
//            .document()
    }

    override fun getPlannedWorkoutSessions(): List<PlannedWorkoutSession> {
        return listOf()
    }
}