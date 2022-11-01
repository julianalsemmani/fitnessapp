package com.groupfive.fitnessapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.groupfive.fitnessapp.calendar.repository.CalendarRepository
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import com.groupfive.fitnessapp.exercise.WorkoutType
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TrainingNotificationService : Service() {

    companion object {
        const val TRAINING_CHANNEL_ID = "training_channel"
    }

    private lateinit var notificationManager: NotificationManager

    private lateinit var calendarRepository: CalendarRepository

    private val notificationTimeOffsetMillis: Long = 15 * 60 * 1000 // 15min

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //TODO: Use proper persistent repository here
        calendarRepository = TestCalendarRepository.instance()

        setupNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        showNotificationsForApproachingWorkoutSessions()

        scheduleNextServiceWakeupAlarm()

        stopSelf()

        return START_NOT_STICKY
    }

    private fun showNotificationsForApproachingWorkoutSessions() {
        runBlocking {
            calendarRepository.getPlannedWorkoutSessions().forEach() { plannedWorkoutSession ->
                if(Instant.now().isBefore(plannedWorkoutSession.startTime)
                    && Instant.now().plusMillis(notificationTimeOffsetMillis).isAfter(plannedWorkoutSession.startTime)) {

                    showNotification(
                        plannedWorkoutSession.id.hashCode(),
                        Duration.between(Instant.now(), plannedWorkoutSession.startTime).toMinutes().toInt(),
                        plannedWorkoutSession.workoutType)
                }
            }
        }
    }

    private fun scheduleNextServiceWakeupAlarm() {
        // Pending intent to start this service
        val serviceIntent = Intent(applicationContext, TrainingNotificationService::class.java)
        val pendingServiceIntent = PendingIntent.getService(
            applicationContext,
            0,
            serviceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE)!! as AlarmManager

        // Find the next workout session that will need to be notified
        runBlocking {
            val nextWorkoutSession = calendarRepository.getPlannedWorkoutSessions().find { it.startTime.isAfter(Instant.now().plusMillis(notificationTimeOffsetMillis)) }
            if(nextWorkoutSession != null) {
                // Schedule that this service is to be started when it is time for notification to be shown
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        nextWorkoutSession.startTime.minusMillis(notificationTimeOffsetMillis).toEpochMilli(),
                        pendingServiceIntent),
                    pendingServiceIntent)

                Log.d(javaClass.simpleName, "Scheduled alarm at ${LocalDateTime.ofInstant(nextWorkoutSession.startTime.minusMillis(notificationTimeOffsetMillis), ZoneId.systemDefault())} ${alarmManager.nextAlarmClock}")
            } else {
                // Cancel intent in case it is already planned
                pendingServiceIntent.cancel()
                alarmManager.cancel(pendingServiceIntent)
            }
        }
    }

    private fun showNotification(notificationId:Int, minutesLeft:Int, workoutType:WorkoutType) {
        // When user presses notification the app should be launched
        val activityIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent: PendingIntent =  PendingIntent.getActivity(
            applicationContext,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Notification contents
        val notification = NotificationCompat.Builder(applicationContext, TRAINING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_training_reminder)
            .setContentTitle("Training reminder")
            .setContentText("This is reminder for you to train. Your ${workoutType.name} starts in $minutesLeft minutes. ")
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .build()

        // Shows the notification
        notificationManager.notify(notificationId, notification)
    }

    private fun setupNotificationChannel() {
        // The notification channel that our notifications belong to
        val channel = NotificationChannel(
            TRAINING_CHANNEL_ID,
            "Training Reminder Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used for training reminder notification."

        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }
}