package com.groupfive.fitnessapp.exercise

import com.google.mlkit.vision.pose.Pose

class ExercisePoseConstraints(vararg constraintArgs: ExercisePoseConstraint) {
    data class ConstraintResult(val constraint: ExercisePoseConstraint, val result: ExercisePoseConstraint.ConstraintResult)

    data class ConstraintsResult(
        val failingConstraints: List<ConstraintResult>,
        val passingConstraints: List<ConstraintResult>) {

        fun allPassed(): Boolean {
            return failingConstraints.isEmpty()
        }
    }

    private val constraints: List<ExercisePoseConstraint> = listOf(*constraintArgs)

    fun checkPose(pose: Pose): ConstraintsResult {
        val passingConstraints = ArrayList<ConstraintResult>()
        val failingConstraints = ArrayList<ConstraintResult>()

        constraints.forEach() {
            val result = it.evaluate(pose)
            if(result.passed) {
                passingConstraints.add(ConstraintResult(it, result))
            } else {
                failingConstraints.add(ConstraintResult(it, result))
            }
        }

        return ConstraintsResult(
            passingConstraints = passingConstraints,
            failingConstraints = failingConstraints
        )
    }
}