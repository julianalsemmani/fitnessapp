package com.groupfive.fitnessapp.util

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.dot
import dev.romainguy.kotlin.math.length
import kotlin.math.*

class PoseUtils {
    companion object {
        fun getAngle(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
            val a = Float3(
                firstPoint.position3D.x - midPoint.position3D.x,
                firstPoint.position3D.y - midPoint.position3D.y,
                firstPoint.position3D.z - midPoint.position3D.z
            )
            val b = Float3(
                lastPoint.position3D.x - midPoint.position3D.x,
                lastPoint.position3D.y - midPoint.position3D.y,
                lastPoint.position3D.z - midPoint.position3D.z
            )
            val dot = dot(a, b)
            val aLength = length(a)
            val bLength = length(b)
            val cos = dot / (aLength * bLength)

            var result = Math.toDegrees(acos(cos).toDouble())
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