package com.choco_tyranno.todoapp

import com.choco_tyranno.todoapp.data.util.AES256Util
import org.junit.Test
import java.util.*

class EncryptTest {
    @Test
    fun encrypt(){
        val randomId = UUID.randomUUID().toString()
        val encryptedId = AES256Util.instance.encrypt(randomId)
        println("randomId:$randomId / encryptedId:$encryptedId")
    }
}