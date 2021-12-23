package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class SignUpSubmitView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MaterialButton(context, attrs) {
    fun onSignUpValueChanged(
        emailPass: Boolean,
        passwordPass: Boolean,
        passwordDoubleCheckPass: Boolean
    ) {
        if (emailPass && passwordPass && passwordDoubleCheckPass) {
            isEnabled = true
            return
        }
        isEnabled = false
    }
}