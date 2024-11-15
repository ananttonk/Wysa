package com.anant.wysa.application

import android.app.Application
import com.anant.wysa.database.WysaDataBase
import com.anant.wysa.module.ModuleImpl

class WysaApplication : Application() {
    companion object{
         lateinit var instance : WysaApplication
    }
    private lateinit var appModule: ModuleImpl

    override fun onCreate() {
        super.onCreate()
        instance = this@WysaApplication
        appModule = ModuleImpl(WysaDataBase(this).movieDao())
    }
    fun getAppModule() = appModule
}