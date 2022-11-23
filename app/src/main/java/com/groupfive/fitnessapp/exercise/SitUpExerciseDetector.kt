package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class SitUpExerciseDetector : ExerciseDetector {

    private val safetyMargin = 20.0

    private val sitUpConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER,
            ComparisonType.SMALLER_THAN, 80.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER,
            ComparisonType.SMALLER_THAN, 80.0, safetyMargin),

        ExerciseAngleConstraint(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP,
            ComparisonType.EQUAL, 90.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP,
            ComparisonType.EQUAL, 90.0, safetyMargin)
        )

    private val sitDownConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER,
            ComparisonType.GREATER_THAN, 120.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER,
            ComparisonType.GREATER_THAN, 120.0, safetyMargin),

        ExerciseAngleConstraint(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP,
            ComparisonType.EQUAL, 90.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP,
            ComparisonType.EQUAL, 90.0, safetyMargin)
    )

    private var currentConstraints = sitUpConstraints

    override fun detectRepetition(pose: Pose): ExerciseDetector.Result {
        val constraintsResult = currentConstraints.checkPose(pose)
        var repetition = false

        if(constraintsResult.allPassed()) {
            when(currentConstraints) {
                sitUpConstraints -> {
                    currentConstraints = sitDownConstraints
                }
                sitDownConstraints -> {
                    currentConstraints = sitUpConstraints
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