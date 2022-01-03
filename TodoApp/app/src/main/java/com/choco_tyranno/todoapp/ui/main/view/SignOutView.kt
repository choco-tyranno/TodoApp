package com.choco_tyranno.todoapp.ui.main.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.choco_tyranno.todoapp.ui.main.MainActivity
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.facebook.login.LoginManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.internal.managers.ViewComponentManager
import java.util.concurrent.atomic.AtomicBoolean

class SignOutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
    init {
        setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val activity = context as MainActivity
        fun kakaoSignOut() {
            UserApiClient.instance.logout { error ->
                if (error == null) {
                    Log.d(TAG_DEBUG,"kakao out")
                    activity.startLoginActivity()
                }
            }
        }

        fun firebaseGroupSignOut() {
            val user = Firebase.auth.currentUser
            user?.let {
                Log.d(TAG_DEBUG,"fire base out")
                for (profile in it.providerData) {
                    when (profile.providerId) {
                        "firebase" -> Firebase.auth.signOut()
                        "facebook.com" -> LoginManager.getInstance().logOut()
                    }
                }
                activity.startLoginActivity()
            }
        }
        kakaoSignOut()
        firebaseGroupSignOut()
    }
}