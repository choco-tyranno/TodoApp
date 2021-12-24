package com.choco_tyranno.todoapp.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.data.util.HashProvider
import com.choco_tyranno.todoapp.ui.login.view.SignUpSubmitView
import com.choco_tyranno.todoapp.ui.login.view.SignUpValueResultView
import com.choco_tyranno.todoapp.ui.univ.ToastManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
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
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        handler = Handler(Looper.getMainLooper())
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
        cancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeSignUpValuesBySubmitView() {
        submit.onSignUpValueChanged(
            emailPass = isEmailValid(),
            passwordPass = isPasswordValid(),
            passwordDoubleCheckPass = isPasswordDoubleCheckingPass()
        )
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

    private fun toast(message: String) {
        handler.post { ToastManager.toast(email.context, message) }
    }

    private fun signUp() {
        onLoading()
        checkDuplicateEmailWithFirebase()
    }

    private fun checkDuplicateEmailWithFirebase() {
        //local method
        fun popupSignUpSuccessDialog() {
            handler.post {
                val bottomSheet = SignUpSuccessDialogFragment()
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }

        //local method
        fun popupDuplicateEmailMessage() {
            offLoading()
            toast(email.context.resources.getString(R.string.signUp_duplicateEmail))
        }

        //local method
        fun createAccount() {
            val context = email.context
            auth.createUserWithEmailAndPassword(
                email.text.toString(), HashProvider.hashSHA256(password.text.toString())
            )
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        popupSignUpSuccessDialog()
                        return@addOnCompleteListener
                    }
                    toast(context.resources.getString(R.string.signUp_signUpRequestFail))
                    return@addOnCompleteListener
                }
        }

        //run start point.
        val emailRef = db.collection("users").document("emails")
        db.runTransaction { transaction ->
            val snapshot = transaction.get(emailRef)
            val emailList: ArrayList<String> = snapshot.get("list") as ArrayList<String>
            for (item in emailList) {
                Log.d(TAG, "emailList - item:$item")
            }
            if (emailList.contains(email.text.toString())) {
                Log.d(TAG, "contains.!")
                popupDuplicateEmailMessage()
            } else {
                Log.d(TAG, "not contains.!")
                emailList.add(email.text.toString())
                transaction.update(emailRef, "list", emailList)
                createAccount()
            }
        }.addOnSuccessListener {
            offLoading()
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

    companion object {
        const val TAG = "@@SignUp"
    }
}

data class EmailList(val list: List<String>? = null) {
    fun alreadyExist(email: String): Boolean {
        Log.d("@@EmailList", "list size:$list?.size")
        if (list != null) {
            for (item in list) {
                Log.d("@@EmailList", "list item:$item")
            }
        }
        return list?.contains(email) == true
    }
}
