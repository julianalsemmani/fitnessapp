package com.groupfive.fitnessapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                R.id.ic_home->navController.navigate(R.id.action_global_homeFragment)
                R.id.ic_stats->navController.navigate(R.id.action_global_statsFragment)
                R.id.ic_notifications->navController.navigate(R.id.action_global_notificationsFragment)
                R.id.ic_profile->navController.navigate(R.id.action_global_profileFragment)
            }
            true
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
