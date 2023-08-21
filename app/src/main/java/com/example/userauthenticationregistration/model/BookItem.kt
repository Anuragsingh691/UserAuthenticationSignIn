package com.example.userauthenticationregistration.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookItem(
    val alias: String,
    val hits: Int,
    val id: String,
    val image: String,
    val lastChapterDate: Int,
    val title: String
) : Parcelable