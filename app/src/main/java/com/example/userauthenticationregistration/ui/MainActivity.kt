package com.example.userauthenticationregistration.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.userauthenticationregistration.createFactory
import com.example.userauthenticationregistration.databinding.ActivityMainBinding
import com.example.userauthenticationregistration.model.User
import com.example.userauthenticationregistration.showToast
import com.example.userauthenticationregistration.viewModel.HomeViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
        binding.progressBar.visibility = View.VISIBLE
        homeViewModel.currentUser.observe(this) { user ->
            setUiElements(user)
        }
        homeViewModel.logoutSuccess.observe(this) {
            this.showToast(it)
            startActivity(Intent(this, SignInActivity::class.java))

        }
        homeViewModel.userStorageError.observe(this) {
            this.showToast(it)
        }

        binding.logoutButton.setOnClickListener { view ->
            homeViewModel.logoutUser()
        }
    }

    private fun setUiElements(userData: User) {
        binding.progressBar.visibility = View.GONE
        binding.welcomeTxt.text = "You are logged in successfully"
        binding.userName.text = "Username :- " + userData.name
        binding.userEmail.text = "Email:- " + userData.email
        binding.userCountry.text = "Country :- " + userData.country
    }

    private fun initViewModel() {
        val factory = HomeViewModel().createFactory()
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }
}