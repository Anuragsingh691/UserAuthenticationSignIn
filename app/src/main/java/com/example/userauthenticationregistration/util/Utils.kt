package com.example.userauthenticationregistration.util

import com.example.userauthenticationregistration.model.BookItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.hawk.Hawk
import org.json.JSONArray

class Utils {
    fun getCountriesList(jsonData: String): String {
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

    fun getBookList(jsonData: String): List<BookItem> {
        val gson = GsonBuilder().create()
        return gson.fromJson(jsonData, Array<BookItem>::class.java).toList()
    }

    fun StoreList(bookItem: BookItem){
        val itemList = mutableListOf<BookItem>()
        itemList.add(bookItem)
        Hawk.put("bookList", Gson().toJson(itemList))
    }

    fun getFavBookList(bookListName: String): List<BookItem>? {
        val itemListJson = Hawk.get<String>(bookListName)
        val gson = GsonBuilder().create()
        return gson.fromJson(itemListJson, Array<BookItem>::class.java).toList()
    }
}