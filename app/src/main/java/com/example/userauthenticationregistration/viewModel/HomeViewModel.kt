package com.example.userauthenticationregistration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userauthenticationregistration.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    val logoutSuccess: MutableLiveData<String>? = null
    val userStorageError: MutableLiveData<String>? = null

    fun logoutUser() {
        firebaseAuth.signOut().let {
            logoutSuccess?.value = "Logged out successfully"
        }
    }

    fun getLoggedInUser(): User? {
        val userId = firebaseAuth.currentUser?.uid
        var user: User? = null
        userId?.let { id ->
            firebaseDatabaseReference.child(id).get().addOnSuccessListener { data ->
                if (data.exists()) {
                    user = User(
                        name = data.child("name").value.toString(),
                        email = data.child("email").value.toString(),
                        data.child("country").value.toString()
                    )
                } else {
                    userStorageError?.value = "user data doesnt exit , userId $id"
                }
            }.addOnFailureListener { exception ->
                userStorageError?.value = "failed due to exception $exception"
            }
        }
        return user
    }
}