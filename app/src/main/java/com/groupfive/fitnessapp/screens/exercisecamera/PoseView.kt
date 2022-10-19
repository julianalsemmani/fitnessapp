package com.groupfive.fitnessapp.screens.exercisecamera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PoseView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private data class LandmarkPair(val begin: Int, val end: Int)

    companion object {
        private val LINE_STRIPS = listOf(
            LandmarkPair(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE),
            LandmarkPair(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP),
            LandmarkPair(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER),
            LandmarkPair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW),
            LandmarkPair(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST),

            LandmarkPair(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE),
            LandmarkPair(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP),
            LandmarkPair(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER),
            LandmarkPair(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW),
            LandmarkPair(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST),
        )
    }

    private val bonePaint = Paint(0).apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private var pose: Pose? = null
    private var scale = 1.0f
    private var xOffset = 0f
    private var yOffset = 0f

    fun setPose(pose: Pose, imageWidth: Int, imageHeight: Int) {
        this.pose = pose

        // Calculate graphic transformations for fitting graphic to image from pose analysis
        val viewAspectRatio = width.toFloat() / height
        val imageAspectRatio = imageWidth.toFloat() / imageHeight
        if(viewAspectRatio > imageAspectRatio) {
            scale = width.toFloat() / imageWidth
            xOffset = 0f
            yOffset = (width.toFloat() / imageAspectRatio - height) / 2
        } else {
            scale = height.toFloat() / imageHeight
            xOffset = (height.toFloat() * imageAspectRatio - width) / 2
            yOffset = 0f
        }

        // Mark the view for redraw
        invalidate()
    }

    private fun translateX(x: Float): Float {
        return x * scale - xOffset
    }

    private fun translateY(y: Float): Float {
        return y * scale - yOffset
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(pose == null) return

        val pose = this.pose as Pose

        if(pose.allPoseLandmarks.isNotEmpty()) {
            canvas.apply {
                // Loop through all line strips and draw line between detected pose landmarks
                LINE_STRIPS.forEach {
                    val begin = pose.getPoseLandmark(it.begin)!!
                    val end = pose.getPoseLandmark(it.end)!!
                    canvas.drawLine(
                        translateX(begin.position.x), translateY(begin.position.y),
                        translateX(end.position.x), translateY(end.position.y),
                        bonePaint)
                }
            }
        }
    }
}