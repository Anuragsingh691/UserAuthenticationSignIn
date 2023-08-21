package com.example.userauthenticationregistration.util

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.IOException

@Throws(IOException::class)
fun Context.readJsonAsset(fileName: String): String {
    val inputStream = assets.open(fileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charsets.UTF_8)
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun <T : ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}

fun String.isValidPassword(): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z]).{8,}\$".toRegex()
    return passwordRegex.matches(this)
}

fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun View.onClick(context: Context, destination: AppCompatActivity) {
    this.setOnClickListener { view ->
        context.startActivity(Intent(context, destination::class.java))
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}