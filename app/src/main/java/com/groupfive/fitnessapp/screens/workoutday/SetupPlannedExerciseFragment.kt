package com.groupfive.fitnessapp.screens.workoutday

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentSetupPlannedExerciseBinding

class SetupPlannedExerciseFragment : Fragment() {
    private lateinit var binding: FragmentSetupPlannedExerciseBinding

    private val viewModel: SetupPlannedExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupPlannedExerciseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startTimePicker.setOnTimeChangedListener { timePicker, hours, minutes ->
            Log.e(javaClass.simpleName, "$hours, $minutes")
        }

        binding.finishButton.setOnClickListener {
            findNavController().navigate(R.id.action_setupPlannedExerciseFragment_to_workoutDayFragment)
        }
    }
}