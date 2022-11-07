package com.groupfive.fitnessapp.exercise

class ExerciseDetectorFactory {
    companion object {
        fun create(workoutType: WorkoutType): ExerciseDetector {
            return when(workoutType) {
                WorkoutType.SIT_UP -> SitUpExerciseDetector()
                WorkoutType.SQUAT -> SquatExerciseDetector()
                else -> {
                    TODO("Detector not implemented ${workoutType.name}")
                }
            }
        }
    }
}