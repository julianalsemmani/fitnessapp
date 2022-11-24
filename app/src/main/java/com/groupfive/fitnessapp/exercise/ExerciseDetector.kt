package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

interface ExerciseDetector  {
    data class Result(
        val repetition: Boolean,
        val failingConstraints: List<ExercisePoseConstraints.ConstraintResult>,
        val passingConstraints: List<ExercisePoseConstraints.ConstraintResult>)

    /**
     * Returns true when a new repetition is detected
     */
    fun detectRepetition(pose: Pose): Result
}