package com.choco_tyranno.todoapp.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.data.models.User
import com.choco_tyranno.todoapp.data.util.HashProvider
import com.choco_tyranno.todoapp.ui.login.view.SignUpSubmitView
import com.choco_tyranno.todoapp.ui.login.view.SignUpValueResultView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

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
    private lateinit var emailRegex: String
    private lateinit var passwordRegex: String
    private var toast: Toast? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            toast(view.context, "submit!")
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

    private fun signUp() {
        db.collection("users")
            .document(UUID.randomUUID().toString())
            .set(
                User(email.text.toString()
                    , HashProvider.hashSHA256(password.text.toString())
                ))
            .addOnSuccessListener {
                toast(email.context, "success!")
            }
            .addOnFailureListener {
                toast(email.context, "fail..")
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

    private fun toast(context: Context, msg: String) {
        val hotToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        if (toast != null) {
            toast?.cancel()
        }
        toast = hotToast
        toast?.show()
    }
}
