package com.groupfive.fitnessapp.model.user.repository

import com.groupfive.fitnessapp.model.user.User

interface UserRepository {
    suspend fun getLoggedInUser(): User
}