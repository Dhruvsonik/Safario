package com.example.safario.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.safario.data.model.AppDatabase
import com.example.safario.data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "contact_db"
    ).build()

    private val contactDao = db.contactDao()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        loadContacts()
    }

    fun addContact(name: String, phone: String) {
        viewModelScope.launch {
            contactDao.insert(Contact(name = name, phone = phone))
            loadContacts()
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = contactDao.getAllContacts()
        }
    }
}