package com.groupfive.fitnessapp.screens.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.LayoutWorkoutTypeBinding
import com.groupfive.fitnessapp.exercise.WorkoutType

class WorkoutTypeAdapter(
    private val onWorkoutTypeClicked: (WorkoutType)->Unit)
    : RecyclerView.Adapter<WorkoutTypeAdapter.ViewHolder>() {

    // Called when there's a need for a new ViewHolder (a new item in the list/grid)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_workout_type, parent, false)

        // Create the view holder with the corresponding view (list item)
        return ViewHolder(itemView, onWorkoutTypeClicked)
    }

    // Called when data is bound to a specific ViewHolder (and item in the list/grid)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(WorkoutType.values()[position])
    }

    override fun getItemCount(): Int {
        return WorkoutType.values().size
    }

    class ViewHolder (
        private val view: View,
        private val onPlannedWorkoutSessionClicked: (WorkoutType)->Unit)
            : RecyclerView.ViewHolder(view) {

        private val binding = LayoutWorkoutTypeBinding.bind(view)

        fun bind(item: WorkoutType) {
            binding.workoutNameTextView.text = view.context.getString(item.nameResource)
            binding.descriptionTextView.text = view.context.getString(item.descriptionResource)
            binding.workoutImage.setImageDrawable(AppCompatResources.getDrawable(view.context, item.imageResource))

            binding.beginExerciseButton.setOnClickListener {
                onPlannedWorkoutSessionClicked(item)
            }
        }
    }
}