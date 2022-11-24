package com.groupfive.fitnessapp

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.groupfive.fitnessapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    companion object {
        // These fragments do not show bottom navigation
        private val HIDE_NAVBAR_FRAGMENTS = listOf(
            "fragment_login",
            "fragment_register",
            "fragment_exercise_camera")

        // These fragments allow landscape
        private val ALLOW_LANDSCAPE_FRAGMENTS = listOf(
            "fragment_exercise_camera")

        private val HIDE_ACTION_BAR = listOf(
            "fragment_profile",
            "fragment_exercise",
            "fragment_calendar",
            "fragment_stats",
            "fragment_login"
        )
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // Retrieve nav controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_profile->navController.navigate(R.id.action_global_profileFragment)
                R.id.ic_exercise->navController.navigate(R.id.action_global_homeFragment)
                R.id.ic_calendar->navController.navigate(R.id.action_global_calendarFragment)
                R.id.ic_stats->navController.navigate(R.id.action_global_statsFragment)
            }
            true
        }

        navController.addOnDestinationChangedListener {
                _, navDestination: NavDestination, _ ->

            if(navDestination.label != null) {
                val destination = navDestination.label!!
                if(HIDE_NAVBAR_FRAGMENTS.any { it == destination }) {
                    binding.bottomNavigation.visibility = View.GONE
                } else {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                requestedOrientation = if(ALLOW_LANDSCAPE_FRAGMENTS.any { it == destination }) {
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }

                if(HIDE_ACTION_BAR.any { it == destination }) {
                    supportActionBar?.hide()
                } else {
                    supportActionBar?.show()
                }

                // Make sure that fragment navigated to updates bottom navigation
                val selectedItemId = when(destination) {
                    "fragment_profile" -> R.id.ic_profile
                    "fragment_stats" -> R.id.ic_stats
                    "fragment_exercise" -> R.id.ic_exercise
                    "fragment_calendar" -> R.id.ic_calendar
                    else -> -1
                }
                binding.bottomNavigation.menu.forEach {
                    if(it.itemId == selectedItemId) it.isChecked = true
                }
            }

        }

        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navController.popBackStack()
        }
        return true
    }
}
