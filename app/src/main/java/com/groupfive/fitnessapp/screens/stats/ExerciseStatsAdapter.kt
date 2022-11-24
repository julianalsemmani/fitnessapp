package com.groupfive.fitnessapp.screens.stats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.LayoutExerciseStatsBinding
import com.groupfive.fitnessapp.exercise.WorkoutType
import java.util.*

class ExerciseStatsAdapter(
    private val statsViewModel: StatsViewModel)
    : RecyclerView.Adapter<ExerciseStatsAdapter.ViewHolder>() {

    private var workoutReps = listOf<Pair<WorkoutType, Int>>()

    private fun parseData() {
        val workouts = EnumMap<WorkoutType, Int>(WorkoutType::class.java)
        statsViewModel.workoutSessions.value?.forEach { workoutSession ->
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

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        parseData()
        notifyDataSetChanged()
    }

    class ViewHolder (val view: View): RecyclerView.ViewHolder(view) {

        private val binding = LayoutExerciseStatsBinding.bind(view)

        fun bind(item: Pair<WorkoutType, Int>) {
            binding.repsView.text = item.second.toString()
            binding.workoutTypeView.text = view.context.getString(item.first.nameResource)
        }
    }
}