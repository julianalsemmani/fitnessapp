package com.groupfive.fitnessapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.groupfive.fitnessapp.databinding.FragmentWorkoutDayBinding

class WorkoutDayFragment : Fragment() {
    private lateinit var binding: FragmentWorkoutDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutDayBinding.inflate(inflater)



        return binding.root
    }

    companion object {
    }
}