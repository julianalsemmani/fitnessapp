package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PushUpExerciseDetector : ExerciseDetector {

    private val safetyMargin = 10.0

    private val pushUpConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_WRIST,
            ComparisonType.SMALLER_THAN, 10.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_WRIST,
            ComparisonType.SMALLER_THAN, 10.0, safetyMargin)
        )

    private val pushDownConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_WRIST,
            ComparisonType.GREATER_THAN, 20.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_WRIST,
            ComparisonType.GREATER_THAN, 20.0, safetyMargin)
    )

    private var currentConstraints = pushUpConstraints

    override fun detectRepetition(pose: Pose): ExerciseDetector.Result {
        val constraintsResult = currentConstraints.checkPose(pose)
        var repetition = false

        if(constraintsResult.allPassed()) {
            when(currentConstraints) {
                pushUpConstraints -> {
                    currentConstraints = pushDownConstraints
                }
                pushDownConstraints -> {
                    currentConstraints = pushUpConstraints
                    repetition = true
                }
            }
        }

        return ExerciseDetector.Result(
            repetition,
            constraintsResult.failingConstraints,
            constraintsResult.passingConstraints
        )
    }
}