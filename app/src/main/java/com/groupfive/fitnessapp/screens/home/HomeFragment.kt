package com.groupfive.fitnessapp.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentHomeBinding
import com.groupfive.fitnessapp.exercise.WorkoutType
import com.groupfive.fitnessapp.screens.selectworkout.SelectWorkoutFragmentDirections
import com.groupfive.fitnessapp.screens.selectworkout.WorkoutTypeAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.workoutList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.workoutList.adapter = WorkoutTypeAdapter() { selectedWorkoutType ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToExerciseCameraFragment(selectedWorkoutType)
            )
        }

        return binding.root
    }
}