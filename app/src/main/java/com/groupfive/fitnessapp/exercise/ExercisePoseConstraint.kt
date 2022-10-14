package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

interface ExercisePoseConstraint {
    /**
     * Returns true when constraint passes for input pose
     */
    fun evaluate(pose: Pose): Boolean
}