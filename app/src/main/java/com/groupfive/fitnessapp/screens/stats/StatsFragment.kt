package com.groupfive.fitnessapp.screens.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.groupfive.fitnessapp.databinding.FragmentStatsBinding
import com.groupfive.fitnessapp.screens.exercisecamera.ExerciseCameraViewModel
import com.groupfive.fitnessapp.screens.workoutday.WorkoutSessionsAdapter

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding

    private val viewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

        val workoutSessionsRecyclerView = binding.workoutSessionsRecyclerView
        workoutSessionsRecyclerView.adapter = ExerciseStatsAdapter(viewModel)

        workoutSessionsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}