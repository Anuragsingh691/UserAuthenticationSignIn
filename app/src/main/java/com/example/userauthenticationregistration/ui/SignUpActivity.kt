package com.example.userauthenticationregistration.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.userauthenticationregistration.R
import com.example.userauthenticationregistration.viewModel.SignUpViewModel
import com.example.userauthenticationregistration.util.Utils
import com.example.userauthenticationregistration.util.createFactory
import com.example.userauthenticationregistration.databinding.ActivitySignUpBinding
import com.example.userauthenticationregistration.util.isValidEmail
import com.example.userauthenticationregistration.util.isValidPassword
import com.example.userauthenticationregistration.util.readJsonAsset
import com.example.userauthenticationregistration.util.showToast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabaseReference: DatabaseReference
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        signUpViewModel.successToastMsg.observe(this) {
            binding.progressBar.visibility = View.GONE
            this.showToast(it)
            this.startActivity(Intent(this, SignInActivity::class.java))
        }
        signUpViewModel.errorToastMsg?.observe(this) {
            this.showToast(it)
        }


        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        val jsonAsset = this.readJsonAsset("countries.json")
        val countriesResult = Utils().getCountriesList(jsonAsset)
        val countryArray = countriesResult.split(",").toTypedArray()
        val countiesArrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, countryArray)
        binding.countryValue.setAdapter(countiesArrayAdapter)

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val country = binding.countryValue.text.toString()
            val userName = binding.userNameEt.text.toString()

            if (!pass.isValidPassword()) {
                this.showToast(
                    "Password is not matching, please enter min 8 characters with atleast\n" +
                            "one number, special characters[!@#\$%&()], one lowercase letter, and one uppercase letter"
                )
            } else if (!email.isValidEmail()) {

                this.showToast("please enter a valid email")

            } else if (email.isNotEmpty() && country.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && pass == confirmPass && userName.isNotEmpty()) {
                if (pass == confirmPass) {
                    binding.progressBar.visibility = View.VISIBLE
                    signUpViewModel.signUpUser(email, pass, userName, country)
                } else {
                    this.showToast("Password is not matching")
                }
            } else {
                this.showToast("Empty Fields Are not Allowed !!")
            }
        }
    }

    private fun initViewModel() {
        val factory = SignUpViewModel().createFactory()
        signUpViewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]
    }
}