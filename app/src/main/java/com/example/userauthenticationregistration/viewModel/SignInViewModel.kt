package com.example.userauthenticationregistration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
    var successToastMsg = MutableLiveData<String>()
    var errorToastMsg = MutableLiveData<String>()
    fun signInUser(
        email: String,
        pass: String
    ) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                successToastMsg.value = "You are logged in successfully"
            } else {
                errorToastMsg.value = it.exception.toString()
            }
        }
    }
}