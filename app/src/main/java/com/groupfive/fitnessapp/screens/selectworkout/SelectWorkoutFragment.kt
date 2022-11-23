package com.groupfive.fitnessapp.screens.selectworkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.groupfive.fitnessapp.databinding.FragmentSelectWorkoutBinding

class SelectWorkoutFragment : Fragment() {
    lateinit var binding: FragmentSelectWorkoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectWorkoutBinding.inflate(inflater)

        binding.workoutList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.workoutList.adapter = WorkoutTypeAdapter() { selectedWorkoutType ->
            findNavController().navigate(
                SelectWorkoutFragmentDirections.actionSelectWorkoutFragmentToExerciseCameraFragment(selectedWorkoutType)
            )
        }

        return binding.root
    }
}