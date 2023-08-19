package com.example.userauthenticationregistration.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.userauthenticationregistration.viewModel.SignInViewModel
import com.example.userauthenticationregistration.createFactory
import com.example.userauthenticationregistration.databinding.ActivitySignInBinding
import com.example.userauthenticationregistration.isValidEmail
import com.example.userauthenticationregistration.isValidPassword
import com.example.userauthenticationregistration.showToast
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth = FirebaseAuth.getInstance()
        initViewModel()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        signInViewModel.successToastMsg?.observe(this) {
            this.showToast(it)
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        signInViewModel.errorToastMsg?.observe(this) {
            this.showToast(it)
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (!pass.isValidPassword()) {
                this.showToast(
                    "Password is not matching, please enter min 8 characters with atleast\n" +
                            "one number, special characters[!@#\$%&()], one lowercase letter, and one uppercase letter"
                )
            } else if (!email.isValidEmail()) {

                this.showToast("please enter a valid email")

            } else if (email.isNotEmpty() && pass.isNotEmpty()) {
                signInViewModel.signInUser(email, pass)
            } else {
                this.showToast("Empty Fields Are not Allowed !!")
            }
        }
    }


    private fun initViewModel() {
        val factory = SignInViewModel().createFactory()
        signInViewModel = ViewModelProvider(this, factory)[SignInViewModel::class.java]
    }
}