package com.example.userauthenticationregistration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userauthenticationregistration.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class HomeViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    val logoutSuccess = MutableLiveData<String>()
    val userStorageError = MutableLiveData<String>()
    val currentUser = MutableLiveData<User>()

    init {
        getLoggedInUser()
    }

    fun logoutUser() {
        firebaseAuth.signOut().let {
            logoutSuccess.value = "Logged out successfully"
        }
    }

    private fun getLoggedInUser() {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let { id ->
            firebaseDatabaseReference.child(id).get().addOnSuccessListener { data ->
                if (data.exists()) {
                    currentUser.value = User(
                        name = data.child("name").value.toString().capitalize(Locale.ROOT),
                        email = data.child("email").value.toString(),
                        country = data.child("country").value.toString().capitalize(Locale.ROOT)
                    )
                } else {
                    userStorageError.value = "user data doesn't exit , userId $id"
                }
            }.addOnFailureListener { exception ->
                userStorageError.value = "failed due to exception $exception"
            }
        }
    }
}