package com.choco_tyranno.todoapp

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import com.kakao.sdk.common.util.Utility

@HiltAndroidApp
class TodoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "81c1f93c61ea3d94b85c4f7658998726")
    }
}