package com.groupfive.fitnessapp.screens.exercisecamera

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.groupfive.fitnessapp.exercise.ExerciseAngleConstraint
import com.groupfive.fitnessapp.exercise.ExerciseDetector
import com.groupfive.fitnessapp.exercise.ExercisePoseConstraints
import com.groupfive.fitnessapp.util.PoseUtils
import dev.romainguy.kotlin.math.Float2

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

    private val failurePaint = Paint(0).apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val passingPaint = Paint(0).apply {
        color = Color.GREEN
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val argbEvaluator = ArgbEvaluator()

    private var pose: Pose? = null
    private var constraintResult: ExerciseDetector.Result? = null

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

    fun setConstraintsResult(constraintResult: ExerciseDetector.Result) {
        this.constraintResult = constraintResult
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
                    drawLineBetweenLandmarks(it.begin, it.end, canvas, bonePaint)
                }

                // Draw failing constraints
                constraintResult?.failingConstraints?.forEach { failing ->
                    when(failing.constraint) {
                        is ExerciseAngleConstraint -> {
                            failurePaint.color = argbEvaluator.evaluate(
                                failing.result.passRate.toFloat(),
                                Color.RED, Color.GREEN) as Int

                            val first = PoseUtils.float2FromLandmark(pose!!, failing.constraint.firstPoint)
                            val mid = PoseUtils.float2FromLandmark(pose!!, failing.constraint.midPoint)
                            val last = PoseUtils.float2FromLandmark(pose!!, failing.constraint.lastPoint)

                            drawLineBetweenPoints(mid,
                                first - (first - mid)/2f,
                                canvas, failurePaint)
                            drawLineBetweenPoints(mid,
                                last - (last - mid)/2f,
                                canvas, failurePaint)
                        }
                    }
                }

                // Draw passing constraints
//                constraintResult?.passingConstraints?.forEach { passing ->
//                    when(passing.constraint) {
//                        is ExerciseAngleConstraint -> {
//                            drawLineBetweenLandmarks(
//                                passing.constraint.firstPoint,
//                                passing.constraint.midPoint,
//                                canvas, passingPaint)
//                            drawLineBetweenLandmarks(
//                                passing.constraint.midPoint,
//                                passing.constraint.lastPoint,
//                                canvas, passingPaint)
//                        }
//                    }
//                }
            }
        } else {
            // Clear canvas when there is no pose
            canvas.drawColor(Color.TRANSPARENT)
        }

        // Mark the view for redraw
        invalidate()
    }

    private fun drawLineBetweenPoints(a: Float2, b: Float2, canvas: Canvas, paint: Paint) {
        canvas.drawLine(
            translateX(a.x), translateY(a.y),
            translateX(b.x), translateY(b.y),
            paint)
    }

    private fun drawLineBetweenLandmarks(beginId: Int, endId: Int, canvas: Canvas, paint: Paint) {
        val begin = pose!!.getPoseLandmark(beginId)!!
        val end = pose!!.getPoseLandmark(endId)!!
        drawLineBetweenPoints(PoseUtils.float2FromLandmark(begin), PoseUtils.float2FromLandmark(end), canvas, paint)
    }
}