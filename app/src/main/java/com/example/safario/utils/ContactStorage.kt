package com.example.safario.utils

import android.content.Context
import com.example.safario.data.model.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ContactStorage {

    private const val PREF_NAME =
        "contacts_pref"

    private const val CONTACT_KEY =
        "contacts"

    private const val EMERGENCY_KEY =
        "emergency_contacts"

    fun saveContacts(
        context: Context,
        contacts: List<Contact>
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            Gson().toJson(contacts)

        prefs.edit()
            .putString(CONTACT_KEY, json)
            .apply()
    }

    fun getContacts(
        context: Context
    ): List<Contact> {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            prefs.getString(CONTACT_KEY, null)
                ?: return emptyList()

        val type =
            object :
                TypeToken<List<Contact>>() {}.type

        return Gson().fromJson(json, type)
    }

    fun saveEmergencyContacts(
        context: Context,
        contacts: List<Contact>
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            Gson().toJson(contacts)

        prefs.edit()
            .putString(EMERGENCY_KEY, json)
            .apply()
    }

    fun getEmergencyContacts(
        context: Context
    ): List<Contact> {

        val prefs =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            prefs.getString(EMERGENCY_KEY, null)
                ?: return emptyList()

        val type =
            object :
                TypeToken<List<Contact>>() {}.type

        return Gson().fromJson(json, type)
    }
}