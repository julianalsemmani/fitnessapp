package com.groupfive.fitnessapp.model.plannedworkout

import java.time.Instant

data class PlannedWorkoutSession(val id: String, val startTime: Instant, val endTime: Instant)
