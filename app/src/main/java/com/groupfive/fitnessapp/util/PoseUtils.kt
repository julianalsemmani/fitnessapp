package com.groupfive.fitnessapp.util

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.*

class PoseUtils {
    companion object {
        fun getAngle(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
            val a = listOf(
                firstPoint.position3D.x - midPoint.position3D.x,
                firstPoint.position3D.y - midPoint.position3D.y,
                firstPoint.position3D.z - midPoint.position3D.z
            )
            val b = listOf(
                lastPoint.position3D.x - midPoint.position3D.x,
                lastPoint.position3D.y - midPoint.position3D.y,
                lastPoint.position3D.z - midPoint.position3D.z
            )
            val dot = a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
            val aLength = sqrt((a[0].pow(2) + a[1].pow(2) + a[2].pow(2)).toDouble())
            val bLength = sqrt((b[0].pow(2) + b[1].pow(2) + b[2].pow(2)).toDouble())
            val cos = dot / (aLength * bLength)

            var result = Math.toDegrees(acos(cos))
            result = abs(result) // Angle should never be negative
            if (result > 180) {
                result = 360.0 - result // Always get the acute representation of the angle
            }
            return result
        }

        fun getAngle(pose: Pose, firstPoint: Int, midPoint: Int, lastPoint: Int): Double {
            return getAngle(
                pose.getPoseLandmark(firstPoint)!!,
                pose.getPoseLandmark(midPoint)!!,
                pose.getPoseLandmark(lastPoint)!!)
        }
    }
}