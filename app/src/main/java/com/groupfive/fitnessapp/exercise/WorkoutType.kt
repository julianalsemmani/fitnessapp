package com.groupfive.fitnessapp.exercise

import com.groupfive.fitnessapp.R

enum class WorkoutType(val nameResource: Int, val descriptionResource: Int, val imageResource: Int) {
    SQUAT(R.string.squat, R.string.squat_description, R.drawable.squat),
    PUSH_UP(R.string.push_up, R.string.push_up_description, R.drawable.pushup),
    SIT_UP(R.string.sit_up, R.string.sit_up_description, R.drawable.situp);
}