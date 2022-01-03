package com.choco_tyranno.todoapp.ui.login

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.ui.login.databinding.adapters.CertificationCodeInputBoxHelperMode
import com.choco_tyranno.todoapp.ui.login.databinding.adapters.PasswordConfirmMode
import com.choco_tyranno.todoapp.ui.login.databinding.adapters.PasswordMode
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val emailRegex: String = context.resources.getString(R.string.signUp_emailRegexPattern2)
    private val passwordRegex: String =
        context.resources.getString(R.string.signUp_passwordRegexPattern)
    private val regexSpecialCharacterContained: String =
        context.resources.getString(R.string.signUp_regexSpecialCharacterContained)

    /* NAME */
    var name = MutableLiveData<String>("")
//    var email: String = ""
//        set(value) {
//            field = value
//            validatesEmail()
//        }

    /* EMAIL */
    var insertedEmail: String = ""
        set(value) {
            field = value
            email.value = value
            validatesEmail()
        }
    var email = MutableLiveData<String>("")

    /* emailInputBox */
    private val _emailInputBoxEnabled = MutableLiveData<Boolean>(true)
    val emailInputBoxEnabled: LiveData<Boolean> = _emailInputBoxEnabled
    private fun setEmailInputBoxEnabled(value: Boolean) {
        _emailInputBoxEnabled.value = value
    }

    /* certificationCodeReleaser */
    private val _certificationCodeReleaserEnabled = MutableLiveData<Boolean>(false)
    val certificationCodeReleaserEnabled: LiveData<Boolean> =
        _certificationCodeReleaserEnabled

    private fun setCertificationCodeReleaserEnabled(value: Boolean) {
        _certificationCodeReleaserEnabled.value = value
    }

    fun onCertificationCodeReleaserClicked(v: View) {
        setCertificationCodeReleaserEnabled(false)
        setEmailInputBoxEnabled(false)
        setCertificationCodeInputBoxVisibility(View.VISIBLE)
        setCertificationCodeInputBoxEnabled(true)
        setCertificationCodeInputBoxHelperVisibility(View.VISIBLE)
        setCertificationCodeInputBoxHelperMode(CertificationCodeInputBoxHelperMode.DEFAULT)
        releaseCertificationCodeByEmail()
        setCertificationCodeExpirationTimerVisibility(View.VISIBLE)
        startTimer()
    }

    private fun releaseCertificationCodeByEmail() {
        //TODO : send email
    }

    /* certificationCodeInputBox*/
    private val _certificationCodeInputBoxVisibility = MutableLiveData<Int>(View.GONE)
    val certificationCodeInputBoxVisibility: LiveData<Int> =
        _certificationCodeInputBoxVisibility

    private fun setCertificationCodeInputBoxVisibility(value: Int) {
        _certificationCodeInputBoxVisibility.value = value
    }

    private val _certificationCodeInputBoxEnabled = MutableLiveData<Boolean>(true)
    val certificationCodeInputBoxEnabled: LiveData<Boolean> =
        _certificationCodeInputBoxEnabled

    private fun setCertificationCodeInputBoxEnabled(value: Boolean) {
        _certificationCodeInputBoxEnabled.value = value
    }

    /* certificationCodeExpirationTimer */
    private val _certificationCodeExpirationTimerVisibility = MutableLiveData<Int>(View.GONE)
    val certificationCodeExpirationTimerVisibility: LiveData<Int> =
        _certificationCodeExpirationTimerVisibility

    private fun setCertificationCodeExpirationTimerVisibility(value: Int) {
        _certificationCodeExpirationTimerVisibility.value = value
    }

    private val _certificationCodeExpirationTimerText = MutableLiveData<String>("")
    val certificationCodeExpirationTimerText: LiveData<String> =
        _certificationCodeExpirationTimerText

    private fun setCertificationCodeExpirationTimerText(value: String) {
        _certificationCodeExpirationTimerText.value = value
    }

    private fun onCertificationCodeExpired() {
        setCertificationCodeInputBoxHelperMode(CertificationCodeInputBoxHelperMode.EXPIRED)
        setCertificationCodeInputBoxEnabled(false)
        setEmailInputBoxEnabled(true)
        setCertificationCodeReleaserEnabled(true)
    }

    private fun startTimer() {
        val startTimeMillis = System.currentTimeMillis()
        suspend fun count() {
            var registeredMin: Long = 0
            var registeredSec: Long = 0
            fun timeMillisToTimerText(): String {
                val timeSectionDivider = ":"
                var minText: String = ""
                var secText: String = ""
                if (registeredMin < 10) {
                    minText += "0"
                }
                minText += registeredMin
                if (registeredSec < 10) {
                    secText += "0"
                }
                secText += registeredSec
                return minText + timeSectionDivider + secText
            }
            while (System.currentTimeMillis() - startTimeMillis < 5000) {
                delay(300)
                val leftTime = 5000 - (System.currentTimeMillis() - startTimeMillis)
                if (leftTime < 0) return
                val leftMin = leftTime.div(60000)
                val leftSec = leftTime.mod(60000).div(1000).toLong()
                var timeChanged = false
                if (leftMin != registeredMin || leftSec != registeredSec) {
                    timeChanged = true
                }
                if (timeChanged) {
                    registeredMin = leftMin
                    registeredSec = leftSec
                    setCertificationCodeExpirationTimerText(timeMillisToTimerText())
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = async { count() }
            result.await()
            onCertificationCodeExpired()
        }
    }

    /* certificationCodeInputBoxHelper */
    private val _certificationCodeInputBoxHelperVisibility = MutableLiveData<Int>(View.GONE)
    val certificationCodeInputBoxHelperVisibility: LiveData<Int> =
        _certificationCodeInputBoxHelperVisibility

    private fun setCertificationCodeInputBoxHelperVisibility(value: Int) {
        _certificationCodeInputBoxHelperVisibility.value = value
    }

    private val _certificationCodeInputBoxHelperMode =
        MutableLiveData<CertificationCodeInputBoxHelperMode>(CertificationCodeInputBoxHelperMode.DEFAULT)

    val certificationCodeInputBoxHelperMode: LiveData<CertificationCodeInputBoxHelperMode> =
        _certificationCodeInputBoxHelperMode

    private fun setCertificationCodeInputBoxHelperMode(mode: CertificationCodeInputBoxHelperMode) {
        _certificationCodeInputBoxHelperMode.value = mode
    }

    var insertedCertificationCode: String = ""
        set(value) {
            field = value
            certificationCode.value = value
            validatesCertificationCode()
        }
    private val certificationCode = MutableLiveData<String>("")
    private fun clearCertificationCodeInputBox() {
        insertedCertificationCode = ""
    }

    private fun validatesCertificationCode() {
        //TODO : here
    }

    private fun onCertificationCodeValid() {}
    private fun onCertificationCodeInvalid() {}

    /* certificationCodeInputClearButton */
    private val _certificationCodeInputBoxClearButtonVisibility = MutableLiveData<Int>(View.GONE)
    val certificationCodeInputBoxClearButtonVisibility: LiveData<Int> =
        _certificationCodeInputBoxClearButtonVisibility

    private fun setCertificationCodeInputBoxClearButtonVisibility(value: Int) {
        _certificationCodeInputBoxClearButtonVisibility.value = value
    }

    fun onCertificationCodeInputBoxClearButtonClicked(v: View) {
        clearCertificationCodeInputBox()
    }

    /* Password */
    var password: String = ""
        set(value) {
            field = value
            if (value.isEmpty()) {
                _passwordMode.value = PasswordMode.DEFAULT
                return
            }
            validatesPassword()
        }
    private val _passwordMode = MutableLiveData<PasswordMode>(PasswordMode.DEFAULT)
    val passwordMode: LiveData<PasswordMode> = _passwordMode

    //call when password is not empty condition.
    private fun validatesPassword() {
        if (isPasswordValid()) {
            onPasswordValid()
            return
        }
        onPasswordInvalid()
    }

    private fun isPasswordValid(): Boolean {
        return Pattern.compile(passwordRegex)
            .matcher(password)
            .matches()
    }

    private fun onPasswordValid() {
        when (password.length) {
            0 -> throw IllegalArgumentException("Cannot pass 0 length password here.")
            in 1..7 -> {
                _passwordMode.value = PasswordMode.TOO_SHORT
            }
            in 8..20 -> {
                checkPasswordStrength()
            }
            else -> {
                _passwordMode.value = PasswordMode.TOO_LONG
            }
        }
    }

    private fun checkPasswordStrength() {
        val specialCharacterContained =
            Pattern.compile(regexSpecialCharacterContained).matcher(password).matches()
        val isEnoughLength = password.length >= 10
        Log.d(TAG_DEBUG,"specialCharacterContained:$specialCharacterContained")
        Log.d(TAG_DEBUG,"isEnoughLength:$isEnoughLength")
        when {
            specialCharacterContained && isEnoughLength -> {
                _passwordMode.value = PasswordMode.NICE
            }
            (!specialCharacterContained && isEnoughLength) || (specialCharacterContained && !isEnoughLength) -> {
                _passwordMode.value = PasswordMode.GOOD
            }
            !specialCharacterContained && !isEnoughLength -> {
                _passwordMode.value = PasswordMode.FINE
            }
        }
    }

    private fun onPasswordInvalid() {
        _passwordMode.value = PasswordMode.INVALID
    }

    var passwordConfirm: String = ""
        set(value) {
            field = value
            if (value.isEmpty() || password.isEmpty()) {
                _passwordConfirmMode.value = PasswordConfirmMode.DEFAULT
                return
            }
            if (passwordConfirm != password) {
                _passwordConfirmMode.value = PasswordConfirmMode.NOT_MATCH
                return
            }
            _passwordConfirmMode.value = PasswordConfirmMode.MATCH
        }
    private val _passwordConfirmMode =
        MutableLiveData<PasswordConfirmMode>(PasswordConfirmMode.DEFAULT)
    val passwordConfirmMode: LiveData<PasswordConfirmMode> = _passwordConfirmMode

    /* Email*/
    private fun validatesEmail() {
        if (!isEmailValid()) {
            onEmailInvalid()
            return
        }
        onEmailValid()
    }

    private fun isEmailValid(): Boolean {
        if (email.value == null)
            return false
        return Pattern.compile(emailRegex)
            .matcher(email.value!!)
            .matches()
    }

    private fun onEmailValid() {
        setCertificationCodeReleaserEnabled(true)
    }

    private fun onEmailInvalid() {
        setCertificationCodeReleaserEnabled(false)
    }

    /**/

    /* TODO(finally) : remove here*/
    fun testClick(v: View) {}
}