package com.groupfive.fitnessapp

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.groupfive.fitnessapp.databinding.ActivityMainBinding
import com.groupfive.fitnessapp.fragments.HomeFragment
import com.groupfive.fitnessapp.fragments.NotificationsFragment
import com.groupfive.fitnessapp.fragments.ProfileFragment
import com.groupfive.fitnessapp.fragments.StatsFragment
import java.time.Duration

class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val statsFragment = StatsFragment()
    private val notificationsFragment = NotificationsFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeFragment(homeFragment)

        // Notifications
        // activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home->changeFragment(homeFragment)
                R.id.ic_stats->changeFragment(statsFragment)
                R.id.ic_notifications->changeFragment(notificationsFragment)
                R.id.ic_profile->changeFragment(profileFragment)
            }
            true
        }
        setContentView(binding.root)
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
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
