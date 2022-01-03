package com.choco_tyranno.todoapp.ui.login

import androidx.lifecycle.ViewModel
import com.choco_tyranno.todoapp.data.source.MyLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class MyLoginViewModel constructor() : ViewModel() {
//    private val _loginResult = MutableLiveData<MyLoginResult>()
//    val loginResult: LiveData<MyLoginResult> = _loginResult

//    fun login(email: String, password: String) {
//        // can be launched in a separate asynchronous job
//
//        val result = loginRepository.login(email, password)
//        if (result is MyLoginRequestResult.Success) {
//            _loginResult.value =
//                MyLoginResult(success = MyLoggedInUser(id = result.data.id, nickName = result.data.nickName ))
//        } else {
//            _loginResult.value = MyLoginResult(error = R.string.login_failed)
//        }
//    }

    fun isValidEmail(){}
    fun isValidPassword(){}

}