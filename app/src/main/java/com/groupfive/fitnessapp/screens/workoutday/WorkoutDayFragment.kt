package com.groupfive.fitnessapp.screens.workoutday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentWorkoutDayBinding

class WorkoutDayFragment : Fragment() {
    private lateinit var binding: FragmentWorkoutDayBinding

    private val args: WorkoutDayFragmentArgs by navArgs()

    private val viewModel: WorkoutDayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setDay(args.day)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_workoutDayFragment_to_setupPlannedExerciseFragment)
        }

        val plannedSessionsRecyclerView = binding.plannedSessionsRecyclerView
        plannedSessionsRecyclerView.adapter = PlannedWorkoutSessionsAdapter(viewModel)
        plannedSessionsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
    }
}