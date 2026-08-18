package com.realtouchapp.admitted.begin.view

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/*
    作者: RobertChou
    時間: 2023/2/2 上午 11:13
    信箱: didi31139@gmail.com
    說明:
*/
@HiltAndroidApp
class AdmittedApplication :Application(){
    var lat =0.0
    var lon =0.0
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

    }
}