package com.groupfive.fitnessapp

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.JobIntentService
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import java.time.Duration
import java.time.Instant

class MyApp: Application() {
//    private val calendarRepository = TestCalendarRepository.instance()

//    private val notificationHandler = Handler(Looper.getMainLooper())
//    private val notificationInterval = 10000

//    private lateinit var trainingNotificationService: TrainingNotificationService;

    override fun onCreate() {
        super.onCreate()

//        trainingNotificationService = TrainingNotificationService(this)
        Intent(this, TrainingNotificationService::class.java).also { intent ->
            startService(intent)
        }

        // Post notifications every notification interval
//        notificationHandler.postDelayed(object : Runnable {
//            override fun run() {
//                postPendingNotifications()
//                notificationHandler.postDelayed(this, notificationInterval.toLong())
//            }
//        }, notificationInterval.toLong())


    }
}