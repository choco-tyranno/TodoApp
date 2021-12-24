package com.choco_tyranno.todoapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.data.models.User
import com.choco_tyranno.todoapp.data.util.HashProvider
import com.choco_tyranno.todoapp.ui.login.view.SignUpSubmitView
import com.choco_tyranno.todoapp.ui.login.view.SignUpValueResultView
import com.choco_tyranno.todoapp.ui.univ.ToastManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    //    val args : SignUpFragmentArgs by navArgs()
    lateinit var email: AppCompatEditText
    lateinit var password: AppCompatEditText
    lateinit var passwordDuple: AppCompatEditText
    lateinit var emailFormResult: SignUpValueResultView
    lateinit var passwordFormResult: SignUpValueResultView
    lateinit var passwordDoubleCheckResult: SignUpValueResultView
    lateinit var submit: SignUpSubmitView
    lateinit var cancel: MaterialButton
    lateinit var loading: ProgressBar
    private lateinit var emailRegex: String
    private lateinit var passwordRegex: String
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = view.findViewById<ProgressBar>(R.id.signUpFragment_loading)
        emailRegex = view.resources.getString(R.string.signUp_emailRegexPattern)
        passwordRegex = view.resources.getString(R.string.signUp_passwordRegexPattern)
        email = view.findViewById<AppCompatEditText>(R.id.signUpFragment_emailInput)
        email.addTextChangedListener {
            validatesEmail()
            observeSignUpValuesBySubmitView()
        }
        password = view.findViewById<AppCompatEditText>(R.id.signUpFragment_passwordInput)
        password.addTextChangedListener {
            validatesPassword()
            observeSignUpValuesBySubmitView()
        }
        passwordDuple =
            view.findViewById<AppCompatEditText>(R.id.signUpFragment_passwordDoubleCheckInput)
        passwordDuple.addTextChangedListener {
            doubleCheckPassword()
            observeSignUpValuesBySubmitView()
        }
        emailFormResult = view.findViewById(R.id.signUpFragment_emailFormResult)
        passwordFormResult = view.findViewById(R.id.signUpFragment_passwordFormResult)
        passwordDoubleCheckResult = view.findViewById(R.id.signUpFragment_passwordDoubleCheckResult)
        submit = view.findViewById<SignUpSubmitView>(R.id.signUp_submit)
        submit.setOnClickListener {
            signUp()
        }
        cancel = view.findViewById<MaterialButton>(R.id.signUp_cancel)
    }

    private fun observeSignUpValuesBySubmitView() {
        submit.onSignUpValueChanged(
            emailPass = isEmailValid(),
            passwordPass = isPasswordValid(),
            passwordDoubleCheckPass = isPasswordDoubleCheckingPass()
        )
    }

    private fun toggleLoading() {
        when (loading.visibility) {
            View.VISIBLE -> loading.visibility = View.INVISIBLE
            View.INVISIBLE -> loading.visibility = View.VISIBLE
            View.GONE -> {}
        }
    }

    private fun popupDuplicateEmailMessage() {
        toggleLoading()
        ToastManager.toast(email.context, "중복된 이메일입니다. 다른 이메일로 시도해주세요.")
    }

    private fun signUp() {
        toggleLoading()
        checkDuplicateEmailWithFirebase()
    }

    private fun checkDuplicateEmailWithFirebase() {
        fun createAccount() {
            val context = email.context
            auth.createUserWithEmailAndPassword(
                email.text.toString(), HashProvider.hashSHA256(password.text.toString())
            )
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        //성공
                    }
                    if (result.isCanceled) {
                        ToastManager.toast(
                            context,
                            context.resources.getString(R.string.signUp_signUpRequestFail)
                        )
                    }
                }
        }

        fun requestToAddEmail(emailRef: DocumentReference) {
            val context = email.context
            emailRef.update("email_list", FieldValue.arrayUnion(email.text.toString()))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createAccount()
                    } else if (task.isCanceled) {
                        ToastManager.toast(
                            context,
                            context.resources.getString(R.string.signUp_signUpRequestFail)
                        )
                    }
                    toggleLoading()
                }
        }

        val emailRef = db.collection("users").document("emails")
        emailRef.get().addOnSuccessListener { documentSnapshot ->
            val emailList = documentSnapshot.toObject<EmailList>()
            val alreadyExist = emailList?.alreadyExist(email.text.toString())
            if (alreadyExist == true) {
                popupDuplicateEmailMessage()
                return@addOnSuccessListener
            }
            // add email to email_list
            requestToAddEmail(emailRef)
        }
    }

    private fun isEmailValid(): Boolean {
        return Pattern.compile(emailRegex)
            .matcher(email.text.toString())
            .matches()
    }

    private fun isPasswordValid(): Boolean {
        return Pattern.compile(passwordRegex)
            .matcher(password.text.toString())
            .matches()
    }

    private fun isPasswordDoubleCheckingPass(): Boolean {
        return passwordDuple.text.toString() == password.text.toString()
    }

    private fun doubleCheckPassword() {
        if (isPasswordDoubleCheckingPass()) {
            passwordDoubleCheckResult.onValidValue()
            return
        }
        passwordDoubleCheckResult.onInvalidValue()
    }

    private fun validatesEmail() {
        if (isEmailValid()) {
            emailFormResult.onValidValue()

            return
        }

        emailFormResult.onInvalidValue()
    }

    private fun validatesPassword() {
        if (isPasswordValid()) {
            passwordFormResult.onValidValue()
        } else {
            passwordFormResult.onInvalidValue()
        }
    }
}

data class EmailList(val list: List<String>? = null) {
    fun alreadyExist(email: String): Boolean {
        return list?.contains(email) == true
    }
}
