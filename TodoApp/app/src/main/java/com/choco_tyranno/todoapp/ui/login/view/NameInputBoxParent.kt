package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.util.AttributeSet
import com.choco_tyranno.todoapp.ui.login.LoginActivity
import com.choco_tyranno.todoapp.ui.login.SignUpViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.internal.managers.ViewComponentManager

class NameInputBoxParent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputLayout(context, attrs) {
    private lateinit var signUpViewModel: SignUpViewModel
    init {
        val mContext = context as ViewComponentManager.FragmentContextWrapper
        val baseContext = mContext.baseContext as LoginActivity
        signUpViewModel = baseContext.signUpViewModel
        signUpViewModel.name.observe(baseContext,{it : String ->
            if (it.length>10){
                isErrorEnabled = true
                error = "error!"
            }else{
                isErrorEnabled = false
            }
        })
    }
}