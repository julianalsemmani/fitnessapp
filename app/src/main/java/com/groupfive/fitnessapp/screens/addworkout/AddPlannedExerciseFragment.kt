package com.groupfive.fitnessapp.screens.addworkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentAddPlannedExerciseBinding

class AddPlannedExerciseFragment : Fragment() {
    private lateinit var binding: FragmentAddPlannedExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlannedExerciseBinding.inflate(inflater)
        return binding.root
    }
}