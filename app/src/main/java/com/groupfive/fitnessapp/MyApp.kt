package com.groupfive.fitnessapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Notification Channel
        createNotificationChannel()

        // TEST:Showing the notification
        val trainingNotificationService = TrainingNotificationService(this);
        trainingNotificationService.showNotification(1,45)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TrainingNotificationService.TRAINING_CHANNEL_ID,
                "Training Reminder Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Used for training reminder notification."

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}