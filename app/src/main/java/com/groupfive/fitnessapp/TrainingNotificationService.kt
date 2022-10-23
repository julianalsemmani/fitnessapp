package com.groupfive.fitnessapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.groupfive.fitnessapp.exercise.WorkoutType

class TrainingNotificationService(
    private val context: Context
) : Service() {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val TRAINING_CHANNEL_ID = "training_channel"
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Notification Channel
        createNotificationChannel()

        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        alarmManager?.setExact(AlarmManager.ELAPSED_REALTIME, )

        return START_NOT_STICKY
    }

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

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            TRAINING_CHANNEL_ID,
            "Training Reminder Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used for training reminder notification."

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }
}