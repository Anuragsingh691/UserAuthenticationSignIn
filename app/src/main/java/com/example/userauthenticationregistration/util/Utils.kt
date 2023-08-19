package com.example.userauthenticationregistration.util

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
}