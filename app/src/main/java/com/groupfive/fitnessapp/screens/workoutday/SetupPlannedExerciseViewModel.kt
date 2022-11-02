package com.groupfive.fitnessapp.screens.workoutday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.groupfive.fitnessapp.calendar.repository.PlannedWorkoutSession
import com.groupfive.fitnessapp.calendar.repository.TestCalendarRepository
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneOffset

class SetupPlannedExerciseViewModel: ViewModel() {
    //TODO(edward): Swap this out with a repository that communicates with firebase
    private val calendarRepository = TestCalendarRepository.instance()

    private val _day = MutableLiveData<LocalDate>()
    val day: LiveData<LocalDate>
        get() = _day

//    private val _startTimeHours = MutableLiveData<Int>()
//    val durationMinutes: LiveData<Int>
//        get() = _durationMinutes
//
//    private val _durationMinutes = MutableLiveData<Int>()
//    val durationMinutes: LiveData<Int>
//        get() = _durationMinutes

    private val _durationMinutes = MutableLiveData<Int>()
    val durationMinutes: LiveData<Int>
        get() = _durationMinutes

    private val _startTime = MutableLiveData<String>()

//    fun setDay(day: LocalDate) {
//        _day.value = day
//    }

}