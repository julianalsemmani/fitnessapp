package com.groupfive.fitnessapp.user.repository

data class User(
    val uid: String,
    val birthDate: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val height: String,
    val weight: String
)
