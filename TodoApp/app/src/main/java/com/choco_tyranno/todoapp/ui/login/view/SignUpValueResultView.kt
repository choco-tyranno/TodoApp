package com.choco_tyranno.todoapp.ui.login.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.choco_tyranno.todoapp.R

abstract class SignUpValueResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    open fun onValidValue() {
        text = context.resources.getString(R.string.signUp_formValid)
        setTextColor(context.resources.getColor(R.color.green, context.theme))
    }

    open fun onInvalidValue() {
        text = context.resources.getString(R.string.signUp_formInvalid)
        setTextColor(context.resources.getColor(R.color.red, context.theme))
    }
}

class EmailValidateResultView(context: Context, attrs: AttributeSet? = null) :
    SignUpValueResultView(context, attrs)

class PasswordValidateResultView(context: Context, attrs: AttributeSet? = null) :
    SignUpValueResultView(context, attrs)

class PasswordDoubleCheckResultView(context: Context, attrs: AttributeSet? = null) :
    SignUpValueResultView(context, attrs) {
    override fun onValidValue() {
        super.onValidValue()
        text = context.resources.getString(R.string.signUp_passwordMatch)
    }

    override fun onInvalidValue() {
        super.onInvalidValue()
        text = context.resources.getString(R.string.signUp_passwordNotMatch)
    }
}