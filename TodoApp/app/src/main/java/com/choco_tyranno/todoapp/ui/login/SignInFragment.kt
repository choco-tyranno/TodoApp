package com.choco_tyranno.todoapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.choco_tyranno.todoapp.ui.main.MainActivity

import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.data.util.HashProvider
import com.choco_tyranno.todoapp.ui.login.view.FacebookSignInView
import com.choco_tyranno.todoapp.ui.login.view.KakaoSignInView
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.choco_tyranno.todoapp.ui.univ.ToastManager
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient


@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_signin) {
    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var email: AppCompatEditText
    private lateinit var password: AppCompatEditText
    private lateinit var signIn: View
    private lateinit var signUp: View
    private lateinit var kakao: KakaoSignInView
    private lateinit var facebook: FacebookSignInView
    private lateinit var loading: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var handler: Handler
    private var firebaseUser: FirebaseUser? = null
    private var callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        Log.d(TAG_DEBUG, "signInFragment/ viewModel?$viewModel")
        auth = FirebaseAuth.getInstance()
//        auth = Firebase.auth
        db = Firebase.firestore
        handler = Handler(Looper.getMainLooper())
        validatesSession()
    }

    override fun onStart() {
        super.onStart()
        refreshUserAuthState()
    }

    private fun refreshUserAuthState() {
        firebaseUser = auth.currentUser
    }

    private fun validatesSession() {
        Log.d(TAG_DEBUG, "validatesSession")
        fun checkFirebaseUserSession() {
            if (auth.currentUser != null) {
                Log.d(TAG_DEBUG, "login by fire user")
                startMainActivity()
            }
        }

        fun checkKakaoUserSession() {
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                            //로그인 필요
                        } else {
                            //기타 에러
                        }
                    } else {
                        Log.d(TAG_DEBUG, "login by kakao user")
                        //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                        startMainActivity()
                    }
                }
            }
        }
        checkKakaoUserSession()
        checkFirebaseUserSession()
    }

    private fun onLoading() {
        handler.post() {
            loading.visibility = View.VISIBLE
        }
    }

    private fun offLoading() {
        handler.post() {
            loading.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        signIn = view.findViewById<View>(R.id.signInFragment_signIn)
        signUp = view.findViewById<View>(R.id.signInFragment_signUp)
        loading = view.findViewById<ProgressBar>(R.id.signInFragment_loading)
        email = view.findViewById<AppCompatEditText>(R.id.signInFragment_emailInput)
        kakao = view.findViewById<KakaoSignInView>(R.id.signInFragment_kakaoLogin)
        facebook = view.findViewById<FacebookSignInView>(R.id.signInFragment_facebookLogin)
        password = view.findViewById<AppCompatEditText>(R.id.signInFragment_passwordInput)
        signUp.setOnClickListener {
//            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment2()
            findNavController().navigate(action)
        }
        signIn.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                onLoading()
                auth.signInWithEmailAndPassword(
                    email.text.toString(),
                    HashProvider.hashSHA256(password.text.toString())
                )
                    .addOnCompleteListener { task ->
                        offLoading()
                        if (task.isSuccessful) {
                            startMainActivity()
                            return@addOnCompleteListener
                        }
                        toast(it.resources.getString(R.string.signIn_signInValueNotMatch))
                    }
            }

        }

        Log.d(TAG_DEBUG, "viewModel?:$viewModel")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun startMainActivity() {
        handler.post {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun toast(msg: String) {
        handler.post {
            ToastManager.toast(email.context, msg)
        }
    }

    companion object {
        private val TAG = SignInFragment::class.java.simpleName
    }
}