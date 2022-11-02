package com.groupfive.fitnessapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import com.groupfive.fitnessapp.exercise.WorkoutType
import java.time.Duration

class TrainingNotificationService(
    private val context: Context
){
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(notificationId:Int, minutesLeft:Int, workoutType:WorkoutType) {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val activityPendingIntent: PendingIntent =  PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Notification contents
        val notification = NotificationCompat.Builder(context, TRAINING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_training_reminder)
            .setContentTitle("Training reminder")
            .setContentText("This is reminder for you to train. Your ${workoutType.name} starts in $minutesLeft minutes. ")
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .build()

        // Shows the notification
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val TRAINING_CHANNEL_ID = "training_channel"
    }
}