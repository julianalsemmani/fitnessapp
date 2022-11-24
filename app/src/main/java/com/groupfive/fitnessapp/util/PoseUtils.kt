package com.groupfive.fitnessapp.util

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.dot
import dev.romainguy.kotlin.math.length
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2

class PoseUtils {
    companion object {
        fun getAngle2D(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
            var result = Math.toDegrees(
                (atan2(lastPoint.position.y - midPoint.position.y,
                    lastPoint.position.x - midPoint.position.x)
                        - atan2(firstPoint.position.y - midPoint.position.y,
                    firstPoint.position.x - midPoint.position.x)).toDouble())

            result = abs(result) // Angle should never be negative
            if (result > 180) {
                result = 360.0 - result // Always get the acute representation of the angle
            }
            return result
        }

        fun getAngle2D(pose: Pose, firstPoint: Int, midPoint: Int, lastPoint: Int): Double {
            return getAngle2D(
                pose.getPoseLandmark(firstPoint)!!,
                pose.getPoseLandmark(midPoint)!!,
                pose.getPoseLandmark(lastPoint)!!)
        }

        fun getAngle3D(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
            val a = float3FromLandmark(firstPoint) - float3FromLandmark(midPoint)
            val b = float3FromLandmark(lastPoint) - float3FromLandmark(midPoint)
            val cos = dot(a, b) / (length(a) * length(b))

            var result = Math.toDegrees(acos(cos).toDouble())
            result = abs(result) // Angle should never be negative
            if (result > 180) {
                result = 360.0 - result // Always get the acute representation of the angle
            }
            return result
        }

        fun getAngle3D(pose: Pose, firstPoint: Int, midPoint: Int, lastPoint: Int): Double {
            return getAngle3D(
                pose.getPoseLandmark(firstPoint)!!,
                pose.getPoseLandmark(midPoint)!!,
                pose.getPoseLandmark(lastPoint)!!)
        }

        fun float2FromLandmark(pose: Pose, landmarkType: Int): Float2 {
            val landmark = pose.getPoseLandmark(landmarkType)!!
            return float2FromLandmark(landmark)
        }

        fun float2FromLandmark(landmark: PoseLandmark): Float2 {
            return Float2(landmark.position.x, landmark.position.y)
        }

        fun float3FromLandmark(pose: Pose, landmarkType: Int): Float3 {
            val landmark = pose.getPoseLandmark(landmarkType)!!
            return float3FromLandmark(landmark)
        }

        fun float3FromLandmark(landmark: PoseLandmark): Float3 {
            return Float3(landmark.position3D.x, landmark.position3D.y, landmark.position3D.z)
        }
    }
}