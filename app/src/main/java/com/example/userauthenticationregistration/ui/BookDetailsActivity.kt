package com.example.userauthenticationregistration.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.userauthenticationregistration.R
import com.example.userauthenticationregistration.databinding.ActivityBookDetailsBinding
import com.example.userauthenticationregistration.databinding.ActivityMainBinding
import com.example.userauthenticationregistration.model.BookItem
import com.example.userauthenticationregistration.util.Utils
import com.example.userauthenticationregistration.util.showToast

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(this, R.color.white)
        val selectedBookData = intent.getParcelableExtra<BookItem>("clickedBookItem")
        selectedBookData?.let { bookItem ->
            setUIElements(bookItem)
        }
    }

    private fun setUIElements(bookItem: BookItem) {
        binding.bookTitle.text = bookItem.title
        Glide.with(binding.root.context)
            .load(bookItem.image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.bookImageLayout.bookImg)
        binding.hitsValue.text = bookItem.hits.toString()
        binding.aliasValue.text = bookItem.alias
        binding.updatedOnValue.text = bookItem.lastChapterDate.toString()
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setStatusBarColor(context: Context, color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(context, color)
    }
}