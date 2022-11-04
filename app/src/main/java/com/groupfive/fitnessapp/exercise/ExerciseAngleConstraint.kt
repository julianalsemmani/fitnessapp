package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

enum class ComparisonType {
    GREATER_THAN, EQUAL, SMALLER_THAN
}

class ExerciseAngleConstraint(
    val firstPoint: Int,
    val midPoint: Int,
    val lastPoint: Int,
    private val comparisonType: ComparisonType,
    private val angle: Double,
    private val margin: Double) : ExercisePoseConstraint {

    override fun evaluate(pose: Pose): Boolean {
        val currentAngle = ExerciseUtils.getAngle(pose, firstPoint, midPoint, lastPoint)

        return when (comparisonType) {
            ComparisonType.GREATER_THAN -> currentAngle > angle - margin
            ComparisonType.EQUAL -> currentAngle > angle - margin/2 && currentAngle < angle + margin/2
            ComparisonType.SMALLER_THAN -> currentAngle < angle + margin
        }
    }
}