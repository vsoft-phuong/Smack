package com.chatcore.smack.controllers

import android.app.Application
import com.android.volley.toolbox.Volley
import com.chatcore.smack.utilities.SharedPrefs

class App : Application() {

    companion object{
        lateinit var prefs: SharedPrefs
    }
    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }


}