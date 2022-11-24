package com.groupfive.fitnessapp.screens.stats

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.groupfive.fitnessapp.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding

    private val viewModel: StatsViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

        val exerciseStatsRecyclerView = binding.workoutSessionsRecyclerView
        val exerciseStatsAdapter = ExerciseStatsAdapter(viewModel)
        exerciseStatsRecyclerView.adapter = exerciseStatsAdapter
        exerciseStatsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.workoutSessions.observe(viewLifecycleOwner) {
            exerciseStatsAdapter.refresh()
        }

        return binding.root
    }
}