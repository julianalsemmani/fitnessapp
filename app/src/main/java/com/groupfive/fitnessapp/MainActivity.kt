package com.groupfive.fitnessapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.groupfive.fitnessapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Notifications
        // activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

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

            if(navDestination.label?.equals("fragment_login")!! ||
                    navDestination.label?.equals("fragment_register")!! ||
                    navDestination.label?.equals("fragment_exercise_camera")!!) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        setContentView(binding.root)
    }

    // Notification permission
/*    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                // Permission is granted
                val trainingNotificationService = TrainingNotificationService(this)
                trainingNotificationService.showNotification(10,1)
            } else {
                // Permission is denied
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }*/
}
