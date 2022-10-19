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
    private val bonePaint = Paint(0).apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private var pose: Pose? = null

    fun setPose(pose: Pose) {
        this.pose = pose

        // Mark the view for redraw
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawLine(0f, 0f, 400f, 400f, bonePaint)
        }
    }

//    data class
//    companion object {
//        val LineStrips = listOf(
//            PoseLandmark.LEFT_ANKLE
//        )
//    }
}