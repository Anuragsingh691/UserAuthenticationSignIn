package com.example.userauthenticationregistration.ui

import android.R.attr.key
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userauthenticationregistration.R
import com.example.userauthenticationregistration.adapter.BookAdapter
import com.example.userauthenticationregistration.databinding.ActivityMainBinding
import com.example.userauthenticationregistration.model.BookItem
import com.example.userauthenticationregistration.util.SpacesItemDecoration
import com.example.userauthenticationregistration.util.Utils
import com.example.userauthenticationregistration.util.createFactory
import com.example.userauthenticationregistration.util.readJsonAsset
import com.example.userauthenticationregistration.viewModel.HomeViewModel


class MainActivity : AppCompatActivity(),BookAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private var adapter = BookAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
        setStatusBarColor(this, R.color.white)
        binding.greetingText.text = getString(R.string.greeting)
        binding.bookTitleSort.sortTitle.text = getString(R.string.title)
        binding.bookHitSort.sortTitle.text = getString(R.string.hits)
        binding.bookFavSort.sortTitle.text = getString(R.string.favs)
        binding.bookRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.bookRecyclerView.adapter = adapter
        binding.bookRecyclerView.addItemDecoration(
            SpacesItemDecoration(30)
        )
        val jsonAsset = this.readJsonAsset("bookList.json")
        val bookList = Utils().getBookList(jsonAsset)
        adapter.updateData(bookList)
    }

    private fun initViewModel() {
        val factory = HomeViewModel().createFactory()
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setStatusBarColor(context: Context, color: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(context, color)
    }

    override fun onItemClick(bookItem: BookItem?) {
        val mIntent = Intent(this, BookDetailsActivity::class.java)
        mIntent.putExtra("clickedBookItem", bookItem)
        startActivity(mIntent)
    }
}