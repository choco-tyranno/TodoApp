package com.choco_tyranno.todoapp

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.choco_tyranno.todoapp.data.util.HashProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class HashSHA256Test {
    @Test
    fun test() {
        val uuid = UUID.randomUUID().toString()
        val hashedResult1 = HashProvider.hashSHA256(uuid)
        val hashedResult2 = HashProvider.hashSHA256(uuid)
        val hashedResult3 = HashProvider.hashSHA256(uuid)
        Log.d(
            "@@Test", "origin:$uuid \n result1:$hashedResult1 \n" +
                    " / result2:$hashedResult2 / result3:$hashedResult3"
        )
        Assert.assertEquals(hashedResult1, hashedResult2, hashedResult3)
    }
}