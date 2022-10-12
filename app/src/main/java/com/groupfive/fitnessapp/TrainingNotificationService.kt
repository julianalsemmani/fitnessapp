package com.groupfive.fitnessapp

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.Duration

class TrainingNotificationService(
    private val context: Context
){
    fun showNotification(notificationId:Int, minutesLeft:Int) {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val activityPendingIntent: PendingIntent =  PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, TRAINING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_training_reminder)
            .setContentTitle("Training reminder")
            .setContentText("This is reminder for you to train. Your training starts in $minutesLeft minutes.")
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notification.build())
        }
    }

    companion object {
        const val TRAINING_CHANNEL_ID = "training_channel"
    }
}