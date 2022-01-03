package com.choco_tyranno.todoapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.choco_tyranno.todoapp.ui.main.MainActivity
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var handler: Handler
    val signUpViewModel : SignUpViewModel by viewModels()
    //    private lateinit var faceBookCallbackManager: CallbackManager
//    val kakaoLoginSessionCallback: (OAuthToken?, Throwable?) -> Unit by lazy {
//        createKakaoLoginSessionCallback()
//    }
    private var backPressBlocked: Boolean = false

    fun blockBackPress() {
        backPressBlocked = true
    }

    fun unblockBackPress() {
        backPressBlocked = false
    }

    fun startMainActivity() {
        handler.post{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    fun getHandler() = handler

    override fun onBackPressed() {
        if (!backPressBlocked) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "facebook:onActivityResult/ requestCode : $requestCode")
//        faceBookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
//        logoutKakako()
    }

    fun createAccount(email: String, password: String) {
        //로딩
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    //start fragment for 회원가입 완료.
                } else {
                    //회원가입 실패 다이얼 & 재시도,취소 버튼
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
                Log.d(TAG,"provideId:$providerId")
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        handler = Handler(mainLooper)
        auth = Firebase.auth
    }

    private fun createKakaoLoginSessionCallback(): (OAuthToken?, Throwable?) -> Unit {
        return { token, error ->
            if (error != null) {
                Toast.makeText(this@LoginActivity, "로그인 실패?", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
//                testKakaoSession()
            }
        }
    }

    private fun checkLoginSession() {
    }

    private fun testKakaoSession() {
//        checkKakaoToken()
    }

    private fun checkKakaoToken() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {
                Log.i(
                    TAG, "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}" +
                            "\n만료시간: ${tokenInfo.expiresIn} 초"
                )
                checkUserInfo()
            }
        }
    }

    private fun checkUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }

    private fun loginByFacebook(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                }
                if (task.isCanceled) {
                    Log.d(TAG, "signInWithCredential:isCanceled")
                }
                if (task.isComplete) {
                    Log.d(TAG, "signInWithCredential:isComplete")
                    val user = auth.currentUser
                }
            }
    }

    companion object {
        const val TAG = "@@LoginActivity"
    }

}