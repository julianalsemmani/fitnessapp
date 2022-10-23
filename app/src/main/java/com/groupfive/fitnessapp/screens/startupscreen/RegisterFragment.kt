package com.groupfive.fitnessapp.screens.startupscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentLoginBinding
import com.groupfive.fitnessapp.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val controller = findNavController()
            controller.navigate(R.id.action_registerFragment_to_homeFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)

        binding.registerBtn.setOnClickListener {
            if (binding.registerEmailInput.text.toString() != "" && binding.registerPasswordInput.text.toString() != "") {
                auth.createUserWithEmailAndPassword(binding.registerEmailInput.text.toString(), binding.registerPasswordInput.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(javaClass.name, "createUserWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                            val controller = findNavController()
                            controller.navigate(R.id.action_registerFragment_to_homeFragment)
                        } else {
                            Log.w(javaClass.name, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Email/Password can not be empty.", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

}