package com.choco_tyranno.todoapp.ui.login

import com.choco_tyranno.todoapp.data.models.MyLoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class MyLoginResult(
    val success: MyLoggedInUser? = null,
    val error: Int? = null)
