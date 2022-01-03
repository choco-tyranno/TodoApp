package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.choco_tyranno.todoapp.ui.univ.ToastManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.internal.managers.ViewComponentManager

class KakaoSignInView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {

        }
        else if (token != null) {
            val mContext  = context as ViewComponentManager.FragmentContextWrapper
            val baseContext = mContext.baseContext as LoginActivity
            baseContext.startMainActivity()
        }
    }
    init {
        setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }
}