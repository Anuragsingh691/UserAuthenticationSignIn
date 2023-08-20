package com.example.userauthenticationregistration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userauthenticationregistration.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel : ViewModel() {
    var successToastMsg= MutableLiveData<String>()
    var errorToastMsg= MutableLiveData<String>()

    fun signUpUser(
        email: String,
        pass: String,
        userName: String,
        country: String
    ) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = User(name = userName, email = email, country = country)
                val ref = firebaseAuth.currentUser?.uid?.let { uuid ->
                    firebaseDatabaseReference
                        .child(uuid)
                }

                ref?.setValue(user)
                    ?.addOnSuccessListener {
                        successToastMsg.value = "User Registered successfully"
                    }
                    ?.addOnFailureListener { exception ->
                        errorToastMsg?.value = "error while registering user $exception"
                    }
            } else {
                errorToastMsg?.value = "error while registering user ${it.exception}"

            }
        }
    }
}