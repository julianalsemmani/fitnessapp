package com.groupfive.fitnessapp.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.groupfive.fitnessapp.databinding.FragmentProfileBinding
import com.groupfive.fitnessapp.model.user.repository.FirebaseUserRepository
import com.groupfive.fitnessapp.model.user.User
import kotlinx.coroutines.runBlocking

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var userRepository = FirebaseUserRepository()
    private lateinit var userInfo: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            userInfo = userRepository.getLoggedInUser()
        }

        binding.profile.text = "Hello, " + userInfo.firstName
        binding.name.text = "${userInfo.firstName} ${userInfo.lastName}"
    }
}