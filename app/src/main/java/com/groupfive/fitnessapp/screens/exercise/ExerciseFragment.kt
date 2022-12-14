package com.groupfive.fitnessapp.screens.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.groupfive.fitnessapp.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {
    private lateinit var binding: FragmentExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater)

        binding.workoutList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.workoutList.adapter = WorkoutTypeAdapter() { selectedWorkoutType ->
            findNavController().navigate(
                ExerciseFragmentDirections.actionHomeFragmentToExerciseCameraFragment(selectedWorkoutType)
            )
        }

        return binding.root
    }
}