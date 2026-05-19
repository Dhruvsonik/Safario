package com.example.safario.utils

import android.content.Context
import org.json.JSONArray

object HistoryStorage {

    private const val PREF = "history_pref"
    private const val KEY = "history"

    fun savePlace(context: Context, place: String) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val list = getHistory(context).toMutableList()
        list.add(0, place)

        val json = JSONArray()
        list.take(20).forEach { json.put(it) }

        prefs.edit().putString(KEY, json.toString()).apply()
    }

    fun getHistory(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, null) ?: return emptyList()

        val arr = JSONArray(json)
        return List(arr.length()) { arr.getString(it) }
    }
}