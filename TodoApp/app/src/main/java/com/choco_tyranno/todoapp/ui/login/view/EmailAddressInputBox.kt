package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.choco_tyranno.todoapp.ui.login.SignUpViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.internal.managers.ViewComponentManager

class EmailAddressInputBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    private val signUpViewModel: SignUpViewModel

    init {
        val mContext  = context as ContextThemeWrapper
        val fragmentContextWrapper = mContext.baseContext as ViewComponentManager.FragmentContextWrapper
        val loginActivity = fragmentContextWrapper.baseContext as LoginActivity
        signUpViewModel = loginActivity.signUpViewModel
//        signUpViewModel.isWaitingCertificationEmail.observe(loginActivity, Observer { isWaiting : Boolean ->
//            if (isWaiting){
//                makeUnActivated()
//            }else{
//                makeActivated()
//            }
//        })
    }

    private fun makeActivated(){
        isEnabled = true
    }

    private fun makeUnActivated(){
        isEnabled = false
    }


}