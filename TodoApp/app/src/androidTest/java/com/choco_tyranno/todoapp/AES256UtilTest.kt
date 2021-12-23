package com.choco_tyranno.todoapp

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.choco_tyranno.todoapp.data.util.AES256Util
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AES256UtilTest {
    @Test
    fun encrypt(){
        val randomId = UUID.randomUUID().toString()
        repeat(3){
            val encryptedId = AES256Util.instance.encrypt(randomId)
            Log.d("@@Test","randomId:$randomId /  encryptedId: $encryptedId")
        }
    }
}