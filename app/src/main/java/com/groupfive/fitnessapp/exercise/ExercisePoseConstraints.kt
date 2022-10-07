package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

class ExercisePoseConstraints(vararg constraintArgs: ExercisePoseConstraint) {
    private val constraints: List<ExercisePoseConstraint> = listOf(*constraintArgs)

    fun evaluate(pose: Pose): Boolean {
        return constraints.all { it.evaluate(pose) }
    }
}