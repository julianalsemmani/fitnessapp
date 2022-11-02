package com.groupfive.fitnessapp.screens.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDb: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firebaseDb = Firebase.firestore
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val docRef = firebaseDb.collection("users")
                .document(auth.currentUser?.uid!!)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(javaClass.simpleName, auth.currentUser?.uid.toString())
                        Log.d(javaClass.name, "DocumentSnapshot Data: ${document.data}")
                    } else {
                        Log.d(javaClass.name, "No Such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(javaClass.name, "get failed with ", exception)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }
}