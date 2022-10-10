package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

interface ExerciseDetector  {
    fun detectRepetition(pose: Pose): Boolean
}