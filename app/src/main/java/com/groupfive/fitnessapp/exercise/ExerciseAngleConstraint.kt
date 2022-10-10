package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

enum class ComparisonType {
    GREATER_THAN, EQUAL, SMALLER_THAN
}

class ExerciseAngleConstraint(
    private val firstPoint: Int,
    private val midPoint: Int,
    private val lastPoint: Int,
    private val comparisonType: ComparisonType,
    private val angle: Double) : ExercisePoseConstraint {

    override fun evaluate(pose: Pose): Boolean {
        val currentAngle = ExerciseUtils.getAngle(pose, firstPoint, midPoint, lastPoint)

        return when (comparisonType) {
            ComparisonType.GREATER_THAN -> currentAngle > angle
            ComparisonType.EQUAL -> currentAngle == angle
            ComparisonType.SMALLER_THAN -> currentAngle < angle
        }
    }
}