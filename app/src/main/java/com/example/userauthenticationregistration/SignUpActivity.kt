package com.example.userauthenticationregistration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userauthenticationregistration.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import org.json.JSONArray

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

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

                            Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}

@Throws(IOException::class)
fun Context.readJsonAsset(fileName: String): String {
    val inputStream = assets.open(fileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charsets.UTF_8)
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