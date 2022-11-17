package com.groupfive.fitnessapp.screens.workoutday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.model.plannedworkout.PlannedWorkoutSession
import com.groupfive.fitnessapp.databinding.LayoutWorkoutSessionBinding
import com.groupfive.fitnessapp.exercise.WorkoutType
import com.groupfive.fitnessapp.model.workout.WorkoutSession
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.HashMap

class WorkoutSessionsAdapter(
    private val workoutDayViewModel: WorkoutDayViewModel)
    : RecyclerView.Adapter<WorkoutSessionsAdapter.ViewHolder>() {

    private var workoutReps = listOf<Pair<WorkoutType, Int>>()
    init {
        val workouts = EnumMap<WorkoutType, Int>(WorkoutType::class.java)
        workoutDayViewModel.workoutSessions.value?.forEach { workoutSession ->
            workouts.compute(workoutSession.workoutType) { _, currentReps ->
                (currentReps ?: 0) + workoutSession.reps
            }
        }
        workoutReps = workouts.map { Pair(it.key, it.value) }
    }

    // Called when there's a need for a new ViewHolder (a new item in the list/grid)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_workout_session, parent, false)

        // Create the view holder with the corresponding view (list item)
        return ViewHolder(itemView)
    }

    // Called when data is bound to a specific ViewHolder (and item in the list/grid)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workoutReps[position])
    }

    override fun getItemCount(): Int {
        return workoutReps.size
    }

    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {

        private val binding = LayoutWorkoutSessionBinding.bind(view)

        fun bind(item: Pair<WorkoutType, Int>) {
            binding.repsView.text = item.second.toString()

            binding.workoutTypeView.text = item.first.name
        }
    }
}