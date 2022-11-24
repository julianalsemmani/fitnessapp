package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

interface ExercisePoseConstraint {
    data class ConstraintResult(val passed: Boolean, val passRate: Double)

    /**
     * Returns true when constraint passes for input pose, also should return how well it passed
     */
    fun evaluate(pose: Pose): ConstraintResult
}