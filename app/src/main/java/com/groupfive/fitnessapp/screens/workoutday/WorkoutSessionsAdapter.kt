package com.groupfive.fitnessapp.screens.workoutday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import com.groupfive.fitnessapp.databinding.LayoutWorkoutSessionBinding
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class WorkoutSessionsAdapter(
    private val workoutDayViewModel: WorkoutDayViewModel,
    private val onWorkoutSessionClicked: (WorkoutSession)->Unit)
    : RecyclerView.Adapter<WorkoutSessionsAdapter.ViewHolder>() {

    // Called when there's a need for a new ViewHolder (a new item in the list/grid)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_workout_session, parent, false)

        // Create the view holder with the corresponding view (list item)
        return ViewHolder(itemView, onWorkoutSessionClicked)
    }

    // Called when data is bound to a specific ViewHolder (and item in the list/grid)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutSessionAtPosition = workoutDayViewModel.workoutSessions.value?.get(position)!!

        holder.bind(workoutSessionAtPosition)
    }

    override fun getItemCount(): Int {
        return workoutDayViewModel.workoutSessions.value?.size ?: 0
    }

    class ViewHolder (
        view: View,
        private val onWorkoutSessionClicked: (WorkoutSession)->Unit)
            : RecyclerView.ViewHolder(view) {

        private val binding = LayoutWorkoutSessionBinding.bind(view)

        fun bind(item: WorkoutSession) {
            val startTime = LocalDateTime.ofInstant(item.startTime, ZoneId.systemDefault())
            binding.beginTimeView.text = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val endTime = LocalDateTime.ofInstant(item.endTime, ZoneId.systemDefault())
            binding.endTimeView.text = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            binding.repsView.text = item.reps.toString()

            binding.workoutTypeView.text = item.workoutType.name

            binding.root.setOnClickListener {
                onWorkoutSessionClicked(item)
            }
        }
    }
}