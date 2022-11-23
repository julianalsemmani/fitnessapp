package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class SquatExerciseDetector : ExerciseDetector {

    private val safetyMargin = 20.0

    private val squatDownConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP,
            ComparisonType.SMALLER_THAN, 90.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP,
            ComparisonType.SMALLER_THAN, 90.0, safetyMargin)
    )

    private val squatUpConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP,
            ComparisonType.GREATER_THAN, 150.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP,
            ComparisonType.GREATER_THAN, 150.0, safetyMargin)
    )

    private var currentConstraints = squatDownConstraints

    override fun detectRepetition(pose: Pose): ExerciseDetector.Result {
        val constraintsResult = currentConstraints.checkPose(pose)
        var repetition = false

        if(constraintsResult.allPassed()) {
            when(currentConstraints) {
                squatDownConstraints -> {
                    currentConstraints = squatUpConstraints
                }
                squatUpConstraints -> {
                    currentConstraints = squatDownConstraints
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