package com.v1.smartv1alculatorv1.application

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class MyApplication : Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
    }


}