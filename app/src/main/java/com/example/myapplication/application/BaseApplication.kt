package com.example.myapplication.application


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.example.myapplication.BuildConfig
import com.example.myapplication.utils.Preferences
import java.util.*

class BaseApplication : Application() {

    companion object {
        val DEBUG = BuildConfig.DEBUG
        @SuppressLint("StaticFieldLeak")
        var preferences : Preferences? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Locale.setDefault(Locale("in"))
        preferences = Preferences(applicationContext)
    }

}
