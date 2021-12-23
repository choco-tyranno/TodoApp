package com.choco_tyranno.todoapp.data.util

import com.google.android.gms.common.util.Base64Utils
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class AES256Util private constructor(){
    private val keygen = KeyGenerator.getInstance("AES")
    private fun createKey():SecretKey{
        keygen.init(256)
        return keygen.generateKey()
    }

    fun encrypt(input: String): String {
        val key = createKey()
        val arr = ByteArray(16)
        var str = ""
        while (str.length != 16) {
            SecureRandom().nextBytes(arr)
            str = String(arr)
        }
        val iv = getIv(str)
        val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encrypt = cipher.doFinal(input.toByteArray())
        return str + Base64Utils.encode(encrypt)
    }

        private fun getIv(input: String): IvParameterSpec {
        val ivHash = MessageDigest.getInstance("SHA1")
            .digest(input.toByteArray())
        val ivBytes = Arrays.copyOf(ivHash, 16)
        return IvParameterSpec(ivBytes)
    }

    private fun decrypt(input: String, key: SecretKey): String {
        val iv = getIv(input.substring(0, 16))
        val cryptText = input.substring(16, input.length)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val decrypt = cipher.doFinal(Base64Utils.decode(cryptText))
        return String(decrypt)
    }

    companion object{
        val instance :AES256Util = AES256Util()
    }
}