package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.myapplication.fitur.login.LoginActivity

class Preferences(private val context: Context){

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(context.packageName + "_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor
    private val TAG = "Preferences"

    init {
        editor = sharedPreferences.edit()
    }

    companion object {
        private const val KEY_IS_LOGIN = "isLogin"
        private val KEY_USERNAME = "username"
        private val KEY_PASS = "password"

        private val KEY_NAME = "name"
        private val KEY_ADDRESS = "address"
        private val KEY_PHONE = "phone"
        private val KEY_SHOPNAME = "shopName"
    }

    private fun isKeyExist(Key: String): Boolean = sharedPreferences.contains(Key)

    private fun putString(Key: String, value: String) {
        editor.putString(Key, value)
        editor.apply()
    }

    private fun putInt(Key: String, value: Int) {
        editor.putInt(Key, value)
        editor.apply()
    }

    private fun putBoolean(Key: String, value: Boolean) {
        editor.putBoolean(Key, value)
        editor.apply()
    }

    private fun getInt(Key: String): Int = sharedPreferences.getInt(Key, 0)
    private fun getString(Key: String): String = sharedPreferences.getString(Key, "")
    private fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)

    fun initPref() {
        if (!isKeyExist(KEY_IS_LOGIN)) {
            putBoolean(KEY_IS_LOGIN, false)
        }

        if (!isKeyExist(KEY_USERNAME)) {
            putString(KEY_USERNAME, "")
        }

        if (!isKeyExist(KEY_PASS)) {
            putString(KEY_PASS, "")
        }

        if (!isKeyExist(KEY_NAME)) {
            putString(KEY_NAME, "")
        }

        if (!isKeyExist(KEY_ADDRESS)) {
            putString(KEY_ADDRESS, "")
        }

        if (!isKeyExist(KEY_PHONE)) {
            putString(KEY_PHONE, "")
        }

        if (!isKeyExist(KEY_SHOPNAME)) {
            putString(KEY_SHOPNAME, "")
        }
    }

    var isLogin: Boolean
        get() = getBoolean(KEY_IS_LOGIN)
        set(value) = putBoolean(KEY_IS_LOGIN, value)

    var userName : String
        get() = getString(KEY_USERNAME)
        set(value) = putString(KEY_USERNAME,value)

    var pass : String
        get() = getString(KEY_PASS)
        set(value) = putString(KEY_PASS,value)

    var name : String
        get() = getString(KEY_NAME)
        set(value) = putString(KEY_NAME,value)

    var address : String
        get() = getString(KEY_ADDRESS)
        set(value) = putString(KEY_ADDRESS,value)

    var phone : String
        get() = getString(KEY_PHONE)
        set(value) = putString(KEY_PHONE,value)

    var shopName : String
        get() = getString(KEY_SHOPNAME)
        set(value) = putString(KEY_SHOPNAME,value)

    private fun resetPref() {
        if (isKeyExist(KEY_IS_LOGIN)) {
            putBoolean(KEY_IS_LOGIN, false)
        }

        if (isKeyExist(KEY_USERNAME)) {
            putString(KEY_USERNAME, "")
        }

        if (isKeyExist(KEY_PASS)) {
            putString(KEY_PASS, "")
        }

        if (isKeyExist(KEY_NAME)) {
            putString(KEY_NAME, "")
        }

        if (isKeyExist(KEY_ADDRESS)) {
            putString(KEY_ADDRESS, "")
        }

        if (isKeyExist(KEY_PHONE)) {
            putString(KEY_PHONE, "")
        }

        if (isKeyExist(KEY_SHOPNAME)) {
            putString(KEY_SHOPNAME, "")
        }
    }

    fun clearSession() {
        resetPref()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}
