package com.example.safario.utils

import android.content.Context
import org.json.JSONArray

object RecentPlacesStorage {

    private const val PREF = "recent_places"
    private const val KEY = "places"

    fun savePlace(context: Context, place: String) {

        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val old = prefs.getString(KEY, null)

        val list = JSONArray(old ?: "[]")

        list.put(place)

        prefs.edit().putString(KEY, list.toString()).apply()
    }

    fun getPlaces(context: Context): List<String> {

        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, null) ?: return emptyList()

        val array = JSONArray(json)
        val list = mutableListOf<String>()

        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }

        return list.reversed() // latest first
    }
}