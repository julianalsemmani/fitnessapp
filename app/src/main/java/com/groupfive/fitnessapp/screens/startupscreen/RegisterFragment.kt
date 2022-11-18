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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firebaseDb = Firebase.firestore
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
                            firebaseDb.collection("users")
                                .document(auth.currentUser?.uid!!).set(addUserToDb(auth.currentUser?.uid.toString(), "firstname", "lastname", "20", "20", "2022", binding.registerEmailInput.text.toString()))
                                .addOnSuccessListener {
                                    Log.d(javaClass.name, "DocumentSnapshot added with ID: ${auth.currentUser?.uid}")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(javaClass.name, "Error adding document", e)
                                }
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

    fun addUserToDb(uID: String, firstName: String, lastName: String, weight: String, height: String, birthDate: String, email: String): HashMap<String, String> {
        return hashMapOf(
            "uid" to uID,
            "firstName" to firstName,
            "lastname" to lastName,
            "weight" to weight,
            "height" to height,
            "birthDate" to birthDate,
            "email" to email
        )
    }

}