package com.groupfive.fitnessapp

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.ScreenOrientation
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
    }

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // Retrieve nav controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

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
            }

        }

        setContentView(binding.root)
    }
}
