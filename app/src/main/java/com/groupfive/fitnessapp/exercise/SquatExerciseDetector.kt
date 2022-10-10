package com.groupfive.fitnessapp.exercise

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class SquatExerciseDetector : ExerciseDetector {

    val downConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP,
            ComparisonType.SMALLER_THAN, 95.0),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP,
            ComparisonType.SMALLER_THAN, 95.0)
    )

    val upConstraints = ExercisePoseConstraints(
        ExerciseAngleConstraint(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP,
            ComparisonType.GREATER_THAN, 160.0),
        ExerciseAngleConstraint(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP,
            ComparisonType.GREATER_THAN, 160.0)
    )

    var isSquatDown = true

    override fun detectRepetition(pose: Pose): Boolean {
        Log.w(javaClass.name, ExerciseUtils.getAngle(pose,
            PoseLandmark.LEFT_ANKLE,
            PoseLandmark.LEFT_KNEE,
            PoseLandmark.LEFT_HIP).toString())
        if(isSquatDown) {
            if(downConstraints.evaluate(pose)) {
                isSquatDown = false
                Log.w(javaClass.name, "DOWN")
            }
        } else {
            if(upConstraints.evaluate(pose)) {
                isSquatDown = true
                Log.w(javaClass.name, "UP")
                return true
            }
        }
        return false
    }
}