package com.example.userauthenticationregistration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userauthenticationregistration.R
import com.example.userauthenticationregistration.databinding.BookItemBinding
import com.example.userauthenticationregistration.model.BookItem
import com.example.userauthenticationregistration.util.onClick


class BookAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    private var dataList: List<BookItem> = emptyList()

    interface OnItemClickListener {
        fun onItemClick(bookItem: BookItem?)
    }

    fun updateData(newList: List<BookItem>) {
        val oldList = dataList
        val diffUtil: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(ItemDiffCallBack(oldList, newList))
        dataList = newList
        diffUtil.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(BookItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])

        holder.itemView.setOnClickListener {
            listener.onItemClick(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(var binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookItem: BookItem?) {
            bookItem?.let {
                Glide.with(binding.root.context)
                    .load(bookItem.image)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(binding.bookImageLayout.bookImg);
                binding.bookTitle.text = bookItem.title
                binding.hitsValue.text = bookItem.hits.toString()
            }
        }
    }

    class ItemDiffCallBack(
        private var oldList: List<BookItem>,
        private var newList: List<BookItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].id == newList[newItemPosition].id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition] == newList[newItemPosition])
        }

    }
}