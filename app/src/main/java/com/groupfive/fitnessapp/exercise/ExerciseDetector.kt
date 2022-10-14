package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

interface ExerciseDetector  {
    /**
     * Returns true when a new repetition is detected
     */
    fun detectRepetition(pose: Pose): Boolean
}