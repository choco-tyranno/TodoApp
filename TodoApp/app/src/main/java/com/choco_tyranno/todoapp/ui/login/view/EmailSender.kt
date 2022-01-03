package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.lifecycle.Observer
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.choco_tyranno.todoapp.ui.login.SignUpViewModel
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.internal.managers.ViewComponentManager

class EmailSender @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MaterialButton(context, attrs) {
    private lateinit var signUpViewModel: SignUpViewModel

    /*
    * Email validates -> make sendingBtn active
    * SendingBtn clicked -> make emailInputBox, sendingBtn unavailable
    * Make certificationNumberInputBoxParent visible, timeLimiter start(count 5 min).
    * Time out -> show certificationNumberInputBoxParent error("certificationNumber has been expired."),
    * make emailInputBox active, sendingBtn active.
    * certification number confirmed -> make certificationNumberInputBox unactivated, make flag confirmed.
    * */
    init {
//        setOnClickListener {
//            sendCertificationEmail()
//        }
        val mContext  = context as ViewComponentManager.FragmentContextWrapper
//        isEnabled = false
        val baseContext = mContext.baseContext as LoginActivity
        signUpViewModel = baseContext.signUpViewModel

    }

//    private fun onSendCertificationEmail(){
//        signUpViewModel.setIsWaitingCertificationEmail(true)
//        makeUnActivated()
//        sendCertificationEmail()
//    }

    private fun sendCertificationEmail(){
        val emailAddress =signUpViewModel.email
    }

    private fun onStateChange(state : EmailSenderViewState){
        when(state){
            EmailSenderViewState.STATE_ENABLED -> {
                isEnabled = true
            }
            EmailSenderViewState.STATE_UNABLE -> {
                isEnabled = false
            }
        }
    }
}

enum class EmailSenderViewState{
    STATE_UNABLE, STATE_ENABLED
}