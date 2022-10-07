package com.groupfive.fitnessapp.exercise

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class SquatExerciseDetector : ExerciseDetector {
    companion object {
        const val TAG = "SquatExerciseDetector"
    }

    var isSquatDown = true

    override fun detectRepetition(pose: Pose): Boolean {
        Log.w(TAG, ExerciseUtils.getAngle(pose,
            PoseLandmark.LEFT_ANKLE,
            PoseLandmark.LEFT_KNEE,
            PoseLandmark.LEFT_HIP).toString())
        if(isSquatDown) {
            if(ExerciseUtils.getAngle(pose,
                PoseLandmark.LEFT_ANKLE,
                PoseLandmark.LEFT_KNEE,
                PoseLandmark.LEFT_HIP) <= 95) {
                isSquatDown = false
                Log.w(TAG, "DOWN")
            }
        } else {
            if(ExerciseUtils.getAngle(pose,
                    PoseLandmark.LEFT_ANKLE,
                    PoseLandmark.LEFT_KNEE,
                    PoseLandmark.LEFT_HIP) >= 160) {
                isSquatDown = true
                Log.w(TAG, "UP")
                return true
            }
        }
        return false
    }
}