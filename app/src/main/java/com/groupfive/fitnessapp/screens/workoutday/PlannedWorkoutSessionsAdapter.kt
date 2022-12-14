package com.groupfive.fitnessapp.screens.workoutday

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.LayoutPlannedWorkoutSessionBinding
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PlannedWorkoutSessionsAdapter(
    private val workoutDayViewModel: WorkoutDayViewModel,
    private val onPlannedWorkoutSessionClicked: (PlannedWorkoutSession)->Unit)
    : RecyclerView.Adapter<PlannedWorkoutSessionsAdapter.ViewHolder>() {

    // Called when there's a need for a new ViewHolder (a new item in the list/grid)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_planned_workout_session, parent, false)

        // Create the view holder with the corresponding view (list item)
        return ViewHolder(itemView, onPlannedWorkoutSessionClicked)
    }

    // Called when data is bound to a specific ViewHolder (and item in the list/grid)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutSessionAtPosition = workoutDayViewModel.plannedWorkoutSessions.value?.get(position)!!

        holder.bind(workoutSessionAtPosition)
    }

    override fun getItemCount(): Int {
        return workoutDayViewModel.plannedWorkoutSessions.value?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        notifyDataSetChanged()
    }

    class ViewHolder (
        view: View,
        private val onPlannedWorkoutSessionClicked: (PlannedWorkoutSession)->Unit)
            : RecyclerView.ViewHolder(view) {

        private val binding = LayoutPlannedWorkoutSessionBinding.bind(view)

        fun bind(item: PlannedWorkoutSession) {
            val startTime = LocalDateTime.ofInstant(item.startTime, ZoneId.systemDefault())
            binding.beginTimeView.text = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val endTime = LocalDateTime.ofInstant(item.endTime, ZoneId.systemDefault())
            binding.endTimeView.text = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            binding.root.setOnClickListener {
                onPlannedWorkoutSessionClicked(item)
            }
        }
    }
}