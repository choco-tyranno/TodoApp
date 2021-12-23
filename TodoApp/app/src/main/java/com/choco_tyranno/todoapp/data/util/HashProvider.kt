package com.choco_tyranno.todoapp.data.util

import com.facebook.appevents.internal.AppEventUtility.bytesToHex
import java.security.DigestException
import java.security.MessageDigest

class HashProvider {
    companion object {
        fun hashSHA256(msg: String): String {
            val hash: ByteArray
            try {
                val md = MessageDigest.getInstance("SHA-256")
                md.update(msg.toByteArray())
                hash = md.digest()
            } catch (e: CloneNotSupportedException) {
                throw DigestException("couldn't make digest of partial content");
            }
            return bytesToHex(hash)
        }
    }
}