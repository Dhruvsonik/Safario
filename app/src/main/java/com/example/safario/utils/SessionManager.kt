package com.example.safario.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGIN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_TIME = "loginTime"
        private const val KEY_NAME = "name"

        private const val SESSION_DURATION = 7 * 24 * 60 * 60 * 1000L
    }

    // ✅ SAVE SESSION
    fun saveUserSession(email: String, name: String) {
        prefs.edit()
            .putBoolean(KEY_LOGIN, true)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAME, name)
            .putLong(KEY_TIME, System.currentTimeMillis())
            .apply()
    }

    // ✅ CHECK LOGIN
    fun isLoggedIn(): Boolean {
        val isLoggedIn = prefs.getBoolean(KEY_LOGIN, false)
        val loginTime = prefs.getLong(KEY_TIME, 0)

        if (!isLoggedIn || loginTime == 0L) return false

        val currentTime = System.currentTimeMillis()
        return (currentTime - loginTime) <= SESSION_DURATION
    }

    // ✅ GET EMAIL
    fun getUserEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    // ✅ GET NAME (CURRENT SESSION)
    fun getUserName(): String? {
        return prefs.getString(KEY_NAME, null)
    }

    // ✅ REGISTER USER
    fun registerUser(email: String, password: String, name: String) {
        prefs.edit()
            .putString("user_$email", password)
            .putString("name_$email", name)
            .apply()
    }

    // ✅ CHECK IF USER EXISTS
    fun isUserRegistered(email: String): Boolean {
        return prefs.contains("user_$email")
    }

    // ✅ VALIDATE LOGIN
    fun validateUser(email: String, password: String): Boolean {
        val savedPassword = prefs.getString("user_$email", null)
        return savedPassword == password
    }

    // ✅ GET STORED NAME (PER USER)
    fun getStoredName(email: String): String? {
        return prefs.getString("name_$email", null)
    }

    // ✅ UPDATE NAME
    fun updateUserName(newName: String) {
        val email = getUserEmail() ?: return

        prefs.edit()
            .putString("name_$email", newName)
            .putString(KEY_NAME, newName)
            .apply()
    }

    // ✅ LOGOUT
    fun logout() {
        prefs.edit()
            .remove(KEY_LOGIN)
            .remove(KEY_EMAIL)
            .remove(KEY_NAME)
            .remove(KEY_TIME)
            .apply()
    }


    fun updateUserEmail(newEmail: String) {
        prefs.edit().putString("email", newEmail).apply()
    }

    fun updatePassword(password: String) {
        val email = getUserEmail() ?: return
        prefs.edit()
            .putString("user_$email", password)
            .apply()
    }
    fun saveTheme(isDark: Boolean) {
        prefs.edit().putBoolean("dark_mode", isDark).apply()
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }
    fun clearSession() {
        prefs.edit()
            .remove("email")
            .remove("name")
            .apply()
    }
    fun saveUserName(name: String) {

        prefs.edit().putString("user_name", name).apply()
    }


}