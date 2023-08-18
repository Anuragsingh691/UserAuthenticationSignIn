package com.example.userauthenticationregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.userauthenticationregistration.databinding.ActivityMainBinding
import com.example.userauthenticationregistration.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        binding.loggedInSuccessfully.text = "You are logged in successfully!"
        val userEmail = firebaseAuth.currentUser?.email
        binding.userEmail.text = "Hello " + userEmail.toString()

        binding.logoutButton.setOnClickListener { view ->
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}