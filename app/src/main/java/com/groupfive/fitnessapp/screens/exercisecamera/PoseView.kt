package com.groupfive.fitnessapp.screens.exercisecamera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

/**
 * Draws a given pose using canvas
 *
 * Transformation code is taken from Google's sample
 * [code](https://github.com/googlesamples/mlkit/blob/master/android/vision-quickstart/app/src/main/java/com/google/mlkit/vision/demo/GraphicOverlay.java)
 */
class PoseView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private data class LandmarkPair(val begin: Int, val end: Int)

    companion object {
        private val LINE_STRIPS = listOf(
            LandmarkPair(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE),
            LandmarkPair(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP),
            LandmarkPair(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER),
            LandmarkPair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW),
            LandmarkPair(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST),
            LandmarkPair(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_FOOT_INDEX),

            LandmarkPair(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE),
            LandmarkPair(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP),
            LandmarkPair(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER),
            LandmarkPair(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW),
            LandmarkPair(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST),
            LandmarkPair(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_FOOT_INDEX),

            LandmarkPair(PoseLandmark.LEFT_HIP, PoseLandmark.RIGHT_HIP),
            LandmarkPair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER),
            LandmarkPair(PoseLandmark.LEFT_MOUTH, PoseLandmark.RIGHT_MOUTH),
        )
    }

    private val bonePaint = Paint(0).apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private var pose: Pose? = null

    // The factor of overlay View size to image size. Anything in the image coordinates need to be
    // scaled by this amount to fit with the area of overlay View.
    private var scaleFactor = 1.0f

    // The number of horizontal pixels needed to be cropped on each side to fit the image with the
    // area of overlay View after scaling.
    private var postScaleWidthOffset = 0f

    // The number of vertical pixels needed to be cropped on each side to fit the image with the
    // area of overlay View after scaling.
    private var postScaleHeightOffset = 0f

    private var isImageFlipped = false

    fun setPose(pose: Pose?) {
        this.pose = pose
    }

    fun setImageSourceInfo(imageWidth: Int, imageHeight: Int, isFlipped: Boolean) {
        val viewAspectRatio = width.toFloat() / height
        val imageAspectRatio = imageWidth.toFloat() / imageHeight
        postScaleWidthOffset = 0f
        postScaleHeightOffset = 0f
        if (viewAspectRatio > imageAspectRatio) {
            // The image needs to be vertically cropped to be displayed in this view.
            scaleFactor = width.toFloat() / imageWidth
            postScaleHeightOffset = (width.toFloat() / imageAspectRatio - height) / 2
        } else {
            // The image needs to be horizontally cropped to be displayed in this view.
            scaleFactor = height.toFloat() / imageHeight
            postScaleWidthOffset = (height.toFloat() * imageAspectRatio - width) / 2
        }
        isImageFlipped = isFlipped
    }

    /** Adjusts the supplied value from the image scale to the view scale.  */
    private fun scale(imagePixel: Float): Float {
        return imagePixel * scaleFactor
    }

    /**
     * Adjusts the x coordinate from the image's coordinate system to the view coordinate system.
     */
    private fun translateX(x: Float): Float {
        return if (isImageFlipped) {
            width - (scale(x) - postScaleWidthOffset)
        } else {
            scale(x) - postScaleWidthOffset
        }
    }

    /**
     * Adjusts the y coordinate from the image's coordinate system to the view coordinate system.
     */
    private fun translateY(y: Float): Float {
        return scale(y) - postScaleHeightOffset
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(pose != null && pose!!.allPoseLandmarks.isNotEmpty()) {
            canvas.apply {
                // Loop through all line strips and draw line between detected pose landmarks
                LINE_STRIPS.forEach {
                    val begin = pose!!.getPoseLandmark(it.begin)!!
                    val end = pose!!.getPoseLandmark(it.end)!!
                    canvas.drawLine(
                        translateX(begin.position.x), translateY(begin.position.y),
                        translateX(end.position.x), translateY(end.position.y),
                        bonePaint)
                }
            }
        } else {
            // Clear canvas when there is no pose
            canvas.drawColor(Color.TRANSPARENT)
        }

        // Mark the view for redraw
        invalidate()
    }
}