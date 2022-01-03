package com.choco_tyranno.todoapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)

    }

    fun startLoginActivity(){
        handler.post {
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }
    }
}

const val TAG_DEBUG = "@@HOTFIX"