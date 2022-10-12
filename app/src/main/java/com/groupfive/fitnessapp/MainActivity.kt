package com.groupfive.fitnessapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.fragment.app.Fragment
import com.groupfive.fitnessapp.databinding.ActivityMainBinding
import com.groupfive.fitnessapp.fragments.HomeFragment
import com.groupfive.fitnessapp.fragments.NotificationsFragment
import com.groupfive.fitnessapp.fragments.ProfileFragment
import com.groupfive.fitnessapp.fragments.StatsFragment


@ExperimentalGetImage class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val statsFragment = StatsFragment()
    private val notificationsFragment = NotificationsFragment()
    private val profileFragment = ProfileFragment()
    private val cameraExerciseFragment = ExerciseCameraFragment()
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeFragment(homeFragment)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.bottomNavigation.setOnItemSelectedListener {
            Log.e("hei","hei")
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
}
