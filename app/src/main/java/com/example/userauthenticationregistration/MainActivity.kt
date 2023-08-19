package com.example.userauthenticationregistration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.userauthenticationregistration.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    }

    override fun onResume() {
        super.onResume()
        setUiElements()
        binding.logoutButton.setOnClickListener { view ->
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun setUiElements() {
        val userId = firebaseAuth.currentUser?.uid

        userId?.let { id ->
            firebaseDatabaseReference.child(id).get().addOnSuccessListener { data ->
                if (data.exists()) {
                    binding.userName.text =
                        "Username :- " + data.child("name").value.toString()
                    binding.userEmail.text = "Email:- " + data.child("email").value.toString()
                    binding.userCountry.text =
                        "Country :- " + data.child("country").value.toString()

                } else {
                    this.showToast("user data doesnt exit , userId $id")
                }
            }.addOnFailureListener { exception ->
                this.showToast("failed due to exception $exception")
            }
        }
    }
}