package com.am.chatapp.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object{
        private const val PREF_NAME = "user_session"
        private const val IS_LOGGED_IN = "is_logged_in"
    }
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN,isLoggedIn).apply()
    }
    fun isLoggedIn(): Boolean{
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false)
    }

}