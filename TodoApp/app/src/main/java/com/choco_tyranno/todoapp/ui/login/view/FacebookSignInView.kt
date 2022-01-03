package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.NavHostFragment
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.choco_tyranno.todoapp.ui.login.SignInFragment
import com.choco_tyranno.todoapp.ui.main.MainActivity
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.internal.managers.ViewComponentManager

class FacebookSignInView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
    init {
        val facebookLoginButton = LoginButton(context, attrs)
        setOnClickListener {
            //TODO : add loading view using databinding..
            (it.context as ViewComponentManager.FragmentContextWrapper)
            facebookLoginButton.performClick()
        }
        facebookLoginButton.setReadPermissions("email", "public_profile")
        facebookLoginButton.registerCallback(CallbackManager.Factory.create(), object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        val auth = Firebase.auth
        val mContext  = context as ViewComponentManager.FragmentContextWrapper
        val baseContext = mContext.baseContext
        auth.signInWithCredential(credential)
            .addOnCompleteListener(baseContext as LoginActivity) { task ->
                if (!task.isCanceled) {
//                    val user = auth.currentUser
                    baseContext.startMainActivity()
                }
            }
    }
    companion object{
        const val TAG = "@@Facebook"
    }
}