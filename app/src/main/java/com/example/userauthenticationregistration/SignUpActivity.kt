package com.example.userauthenticationregistration

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.userauthenticationregistration.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONArray

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        val jsonAsset = this.readJsonAsset("countries.json")
        val countriesResult = countriesList(jsonAsset)
        val countryArray = countriesResult.split(",").toTypedArray()
        val countiesArrayAdapter = ArrayAdapter(this, R.layout.drop_down_item, countryArray)
        binding.countryValue.setAdapter(countiesArrayAdapter)

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val country = binding.countryValue.text.toString()
            val userName = binding.userNameEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = User(name = userName, email = email, country = country)
                            val ref = firebaseAuth.currentUser?.uid?.let { uuid ->
                                firebaseDatabaseReference
                                    .child(uuid)
                            }

                            ref?.setValue(user)
                                ?.addOnSuccessListener {
                                    this.showToast("registration successful & data added successfully")
                                }
                                ?.addOnFailureListener { exception ->
                                    this.showToast("registration successful but data didn't get stored with exception $exception")
                                }
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            this.showToast("it.exception.toString()")

                        }
                    }
                } else {
                    this.showToast("Password is not matching")
                }
            } else {
                this.showToast("Empty Fields Are not Allowed !!")
            }
        }
    }
}


fun countriesList(jsonData: String): String {
    val jsonArray = JSONArray(jsonData)
    val resCountryList = mutableListOf<String>()

    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val keys = jsonObject.keys()

        while (keys.hasNext()) {
            val key = keys.next() as String
            val innerObject = jsonObject.getJSONObject(key)
            val country = innerObject.getString("country")
            resCountryList.add(country)
        }
    }
    return resCountryList.toString()
}