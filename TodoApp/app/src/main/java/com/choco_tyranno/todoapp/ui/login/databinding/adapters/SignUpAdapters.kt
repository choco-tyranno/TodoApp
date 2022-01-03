package com.choco_tyranno.todoapp.ui.login.databinding.adapters

import com.google.android.material.textfield.TextInputLayout
import androidx.databinding.BindingAdapter
import com.choco_tyranno.todoapp.R
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("app:nameInputBox_errorFlag")
fun setErrorMessage(v: TextInputLayout, name: String) {
    if (name.length <= 10) {
        v.error = null
        return
    }
    v.error = v.resources.getString(R.string.signUp_nameSizeOver)
}

@BindingAdapter("app:certificationCodeInputBoxHelper_mode")
fun setMode(v: MaterialTextView, mode: CertificationCodeInputBoxHelperMode) {
    when (mode) {
        CertificationCodeInputBoxHelperMode.DEFAULT -> {
            v.setTextColor(
                v.resources.getColor(
                    R.color.black,
                    v.context.theme
                )
            )
            v.text = v.resources.getString(R.string.signUp_certificationCodeRequired)
        }
        CertificationCodeInputBoxHelperMode.EXPIRED -> {
            v.setTextColor(
                v.resources.getColor(
                    R.color.design_default_color_error,
                    v.context.theme
                )
            )
            v.text = v.resources.getString(R.string.signUp_certificationCodeExpired)
        }
    }
}

@BindingAdapter("app:password_mode")
fun setMode(v: TextInputLayout, mode: PasswordMode) {
    when (mode) {
        PasswordMode.DEFAULT -> {
            v.error = null
            v.helperText = v.resources.getString(R.string.signUp_passwordForm)
        }
        PasswordMode.INVALID -> {
            v.helperText = null
            v.error = v.resources.getString(R.string.signUp_passwordInvalid)
        }
        PasswordMode.TOO_SHORT -> {
            v.helperText = null
            v.error = v.resources.getString(R.string.signUp_passwordTooShort)}
        PasswordMode.TOO_LONG -> {
            v.helperText = null
            v.error = v.resources.getString(R.string.signUp_passwordTooLong)}
        PasswordMode.FINE -> {
            v.error = null
            v.helperText = v.resources.getString(R.string.signUp_passwordFine)}
        PasswordMode.GOOD -> {
            v.error = null
            v.helperText = v.resources.getString(R.string.signUp_passwordGood)}
        PasswordMode.NICE -> {
            v.error = null
            v.helperText = v.resources.getString(R.string.signUp_passwordNice)}
    }
}

@BindingAdapter("app:passwordConfirm_mode")
fun setMode(v: TextInputLayout, mode: PasswordConfirmMode) {
    when (mode) {
        PasswordConfirmMode.NOT_MATCH -> {
            v.error = v.resources.getString(R.string.signUp_passwordNotMatch)
        }
        PasswordConfirmMode.DEFAULT -> {
            v.error = null
        }
        PasswordConfirmMode.MATCH -> {
            v.error = null
            v.helperText = v.resources.getString(R.string.signUp_passwordMatch)
        }
    }
}

enum class CertificationCodeInputBoxHelperMode {
    DEFAULT, EXPIRED
}

enum class PasswordMode { DEFAULT, INVALID, TOO_SHORT, TOO_LONG, FINE, GOOD, NICE }
enum class PasswordConfirmMode {
    MATCH, NOT_MATCH, DEFAULT
}