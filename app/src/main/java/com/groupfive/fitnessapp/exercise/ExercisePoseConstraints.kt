package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

class ExercisePoseConstraints(private val constraints: List<ExercisePoseConstraint>) {
    fun evaluate(pose: Pose): Boolean {
        return constraints.all { it.evaluate(pose) }
    }
}