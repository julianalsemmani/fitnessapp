package com.groupfive.fitnessapp.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class CalendarUtils {
    companion object {
        fun isInstantInDay(instant: Instant, day: LocalDate): Boolean {
            val startOfDay = day.atStartOfDay(ZoneOffset.systemDefault()).toInstant()
            val endOfDay = day.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).toInstant()

            return instant.isAfter(startOfDay) && instant.isBefore(endOfDay)
        }
    }
}