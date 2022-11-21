package com.groupfive.fitnessapp.screens.startupscreen

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val webClientId = "206715284236-bb3hj871oti5kdvbdves7c4l386e96je.apps.googleusercontent.com"
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val controller = findNavController()
            controller.navigate(R.id.action_loginFragment_to_homeFragment)
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
                            Toast.makeText(context, "Auth success", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            val controller = findNavController()
                            controller.navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Log.w(javaClass.name, "signInWithEmail:failure", task.exception)
                            Toast.makeText(context, "Auth failed", Toast.LENGTH_SHORT).show()
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

        binding.signInWithGoogleBtn.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d(javaClass.simpleName, "onActivityResult: Google SignIn intent result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)

            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to login ", e)

            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(javaClass.name, "signInWithEmail:success")
                    Toast.makeText(context, "Auth success", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val controller = findNavController()
                    controller.navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Log.w(javaClass.name, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Auth failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}