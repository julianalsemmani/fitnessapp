package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose
import com.groupfive.fitnessapp.util.PoseUtils
import kotlin.math.abs

class ExerciseAngleConstraint(
    val firstPoint: Int,
    val midPoint: Int,
    val lastPoint: Int,
    private val comparisonType: ComparisonType,
    private val angle: Double,
    private val margin: Double) : ExercisePoseConstraint {

    override fun evaluate(pose: Pose): ExercisePoseConstraint.ConstraintResult {
        val currentAngle = PoseUtils.getAngle2D(pose, firstPoint, midPoint, lastPoint)

        val result = when (comparisonType) {
            ComparisonType.GREATER_THAN -> ExercisePoseConstraint.ConstraintResult(
                currentAngle > angle - margin,
                currentAngle / angle
            )
            ComparisonType.EQUAL -> ExercisePoseConstraint.ConstraintResult(
                currentAngle > angle - margin/2 && currentAngle < angle + margin/2,
                if(currentAngle < angle)
                    currentAngle / angle
                else
                    1.0 - abs(currentAngle - angle) / (180.0 - angle)
            )
            ComparisonType.SMALLER_THAN -> ExercisePoseConstraint.ConstraintResult(
                currentAngle < angle + margin,
                1.0 - (currentAngle - angle) / (180.0 - angle)
            )
        }

//        Log.e(javaClass.simpleName, angle.toString() + " - " + currentAngle.toString() + " - " + result.passRate.toString())

        return result
    }
}