package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PushUpExerciseDetector : ExerciseDetector {

    private val safetyMargin = 15.0

    private val pushUpConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_EAR, PoseLandmark.LEFT_WRIST,
            ComparisonType.GREATER_THAN, 40.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_EAR, PoseLandmark.RIGHT_WRIST,
            ComparisonType.GREATER_THAN, 40.0, safetyMargin),

        ExerciseAngleConstraint(PoseLandmark.LEFT_WRIST, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_SHOULDER,
            ComparisonType.GREATER_THAN, 160.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_WRIST, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_SHOULDER,
            ComparisonType.GREATER_THAN, 160.0, safetyMargin)
        )

    private val pushDownConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_EAR, PoseLandmark.LEFT_WRIST,
            ComparisonType.SMALLER_THAN, 30.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_EAR, PoseLandmark.RIGHT_WRIST,
            ComparisonType.SMALLER_THAN, 30.0, safetyMargin),

        ExerciseAngleConstraint(PoseLandmark.LEFT_WRIST, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_SHOULDER,
            ComparisonType.SMALLER_THAN, 60.0, safetyMargin),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_WRIST, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_SHOULDER,
            ComparisonType.SMALLER_THAN, 60.0, safetyMargin)
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