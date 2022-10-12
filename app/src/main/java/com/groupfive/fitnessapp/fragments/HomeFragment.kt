package com.groupfive.fitnessapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.cameraBtn.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.action_homeFragment_to_exerciseCameraFragment)
        }

        binding.calenderBtn.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.action_homeFragment_to_calendarFragment)
        }

        return binding.root
    }
}