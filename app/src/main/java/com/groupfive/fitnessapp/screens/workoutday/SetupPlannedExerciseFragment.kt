package com.groupfive.fitnessapp.screens.workoutday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.groupfive.fitnessapp.databinding.FragmentSetupPlannedExerciseBinding
import java.time.ZoneOffset

class SetupPlannedExerciseFragment : Fragment() {
    private lateinit var binding: FragmentSetupPlannedExerciseBinding

    private val args: SetupPlannedExerciseFragmentArgs by navArgs()

    private val viewModel: SetupPlannedExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setDay(args.day)
//        if(args.plannedWorkoutSession != null) {
//            viewModel.setPlannedWorkoutSession(args.plannedWorkoutSession!!)
//        }
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
            val startOfDay = args.day.atStartOfDay(ZoneOffset.systemDefault()).toInstant()
            val newStartTime = startOfDay.plusSeconds((minutes*60).toLong()).plusSeconds((hours*3600).toLong())
            viewModel.setStartTime(newStartTime)
        }

        binding.startTimePicker.setIs24HourView(android.text.format.DateFormat.is24HourFormat(context))

        binding.finishButton.setOnClickListener {
            viewModel.submitPlannedWorkoutSession()

            findNavController().navigate(
                SetupPlannedExerciseFragmentDirections.actionSetupPlannedExerciseFragmentToWorkoutDayFragment(
                args.day
            ))
        }
    }
}