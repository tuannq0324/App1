package com.example.app1
//
import android.app.Application
import com.example.app1.api.RequesterImpl

//import dagger.hilt.android.HiltAndroidApp
//
//@HiltAndroidApp
class BaseApplication: Application() {

    companion object {
        lateinit var instance: Application

        val requester by lazy {
            RequesterImpl()
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

}