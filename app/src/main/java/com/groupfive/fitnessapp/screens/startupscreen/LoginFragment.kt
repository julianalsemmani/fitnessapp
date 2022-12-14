package com.groupfive.fitnessapp.screens.startupscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val controller = findNavController()
            controller.navigate(R.id.action_loginFragment_to_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)

        binding.loginBtn.setOnClickListener {
            if (binding.loginEmailInput.text.toString() != "" && binding.loginPasswordInput.text.toString() != "") {
                auth.signInWithEmailAndPassword(binding.loginEmailInput.text.toString(), binding.loginPasswordInput.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(javaClass.name, "signInWithEmail:success")
                            Toast.makeText(context, "Login Successful.", Toast.LENGTH_SHORT).show()

                            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                        } else {
                            Log.w(javaClass.name, "signInWithEmail:failure", task.exception)
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Email/Password can not be empty.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.registerBtn.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }
}