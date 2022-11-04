package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

class ExercisePoseConstraints(vararg constraintArgs: ExercisePoseConstraint) {
    data class Result(
        val failingConstraints: List<ExercisePoseConstraint>,
        val passingConstraints: List<ExercisePoseConstraint>) {

        fun allPassed(): Boolean {
            return failingConstraints.isEmpty()
        }
    }

    private val constraints: List<ExercisePoseConstraint> = listOf(*constraintArgs)

    fun checkPose(pose: Pose): Result {
        val passingConstraints = ArrayList<ExercisePoseConstraint>()
        val failingConstraints = ArrayList<ExercisePoseConstraint>()

        constraints.forEach() {
            if(it.evaluate(pose)) {
                passingConstraints.add(it)
            } else {
                failingConstraints.add(it)
            }
        }

        return Result(
            passingConstraints = passingConstraints,
            failingConstraints = failingConstraints
        )
    }
}