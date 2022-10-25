package com.groupfive.fitnessapp

import android.app.Application
import android.content.Intent

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Intent(this, TrainingNotificationService::class.java).also { intent ->
            startService(intent)
        }

    }
}