package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

interface ExercisePoseConstraint {
    fun evaluate(pose: Pose): Boolean
}