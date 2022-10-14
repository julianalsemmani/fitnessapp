package com.groupfive.fitnessapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import java.time.Duration
import java.time.Instant

class MyApp: Application() {
    private val calendarRepository = TestCalendarRepository.instance()

    override fun onCreate() {
        super.onCreate()

        // Notification Channel
        createNotificationChannel()

        // TEST:Showing the notification
        val trainingNotificationService = TrainingNotificationService(this);
        calendarRepository.getPlannedWorkoutSessions().forEachIndexed { index, plannedWorkoutSession ->
            run {
                val durationUntil = Duration.between(Instant.now(), plannedWorkoutSession.startTime)
                if(durationUntil.toMinutes() < 30) {
                    trainingNotificationService.showNotification(index,
                        durationUntil.toMinutes().toInt(),
                        plannedWorkoutSession.workoutType
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
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