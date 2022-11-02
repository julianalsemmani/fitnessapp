package com.groupfive.fitnessapp.user.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository : UserRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override suspend fun getLoggedInUser(): User {

        val userDoc = userCollection()
            .get()
            .await()

        return User(
            userDoc.data?.get("uid")!! as String,
            userDoc.data?.get("birthDate")!! as String,
            userDoc.data?.get("email")!! as String,
            userDoc.data?.get("firstName")!! as String,
            userDoc.data?.get("lastname")!! as String,
            userDoc.data?.get("height")!! as String,
            userDoc.data?.get("weight")!! as String
        )
    }

    private fun userCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid!!)
}