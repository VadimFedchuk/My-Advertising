package com.vadimfedchuk.myadvertising.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class ShPreferences private constructor(){

    companion object {
        private const val APP_PREFERENCES = "settings_advertising"

        private const val PREF_TOKEN = "token"
        private const val PREF_TOKEN_FIREBASE = "firebase"
        private const val PREF_NAME_USER = "name_user"
        private const val PREF_PHONE_USER = "phone_user"
        private const val PREF_PASSWORD_USER = "password_user"
        private const val PREF_AVATAR_USER = "avatar_user"

        fun setToken(value: String?, context: Context) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_TOKEN, value)
            editor.apply()
        }

        fun getToken(context: Context):String? {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            return sharedPref.getString(PREF_TOKEN, null)
        }

        fun setTokenFirebase(value: String?, context: Context) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_TOKEN_FIREBASE, value)
            editor.apply()
        }

        fun getTokenFirebase(context: Context):String? {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            return sharedPref.getString(PREF_TOKEN_FIREBASE, "")
        }

        fun setNameUser(value: String, context: Context) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_NAME_USER, value)
            editor.apply()
        }

        fun getNameUser(context: Context):String? {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            return sharedPref.getString(PREF_NAME_USER, null)
        }

        fun setPhoneUser(value: String, context: Context) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_PHONE_USER, value)
            editor.apply()
        }

        fun getPhoneUser(context: Context):String? {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            return sharedPref.getString(PREF_PHONE_USER, null)
        }

        fun setPasswordUser(value: String, context: Context) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_PASSWORD_USER, value)
            editor.apply()
        }

        fun getPasswordUser(context: Context):String? {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            return sharedPref.getString(PREF_PASSWORD_USER, null)
        }

        fun setAvatarUser(value: String, context: Context) {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(PREF_AVATAR_USER, value)
            editor.apply()
        }

        fun getAvatarUser(context: Context):String? {
            val sharedPref: SharedPreferences = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            return sharedPref.getString(PREF_AVATAR_USER, null)
        }
    }
}