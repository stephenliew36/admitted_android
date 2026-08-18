package com.realtouchapp.oh9hapilibrary.encryption

import android.content.Context
import java.io.UnsupportedEncodingException


class EnCryption {
    companion object {
        //取得加密後的MD5Hash字串
        fun enCryptionByMd5Hash(value: String): String {
            return Cryption.byte2Hex(Cryption.MD5Hash(value.toByteArray(Charsets.UTF_8)))
        }

        //取得加密後的SHA256Hash字串
        fun enCryptionBySHA256Hash(value: String): String {
            return Cryption.byte2Hex(Cryption.SHA256Hash(value.toByteArray(Charsets.UTF_8)))
        }


        fun deCryption(jsonString: String, AES_KEY: String, IV_KEY: String): String {
            var encryptedData: ByteArray? = Cryption.decodeBase64String(jsonString)
//            Log.i("oh9hPost", "origin: $jsonString")
            var aesKey: ByteArray? = null
            try {
                aesKey = Cryption.SHA256Hash(AES_KEY.toByteArray(Charsets.UTF_8))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            var ivKey: ByteArray? = null
            try {
                ivKey = Cryption.MD5Hash(IV_KEY.toByteArray(Charsets.UTF_8))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            encryptedData = Cryption.decryptAES256(encryptedData, aesKey, ivKey)
            return encryptedData!!.toString(Charsets.UTF_8)
        }


        fun enCryption(jsonString: String, AES_KEY: String, IV_KEY: String): String {
            var encryptedData: ByteArray? = null
            try {
                encryptedData = jsonString.toByteArray(Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

//            Log.i("oh9hPost", "origin: $jsonString")
            var aesKey: ByteArray? = null
            try {
                aesKey = Cryption.SHA256Hash(AES_KEY.toByteArray(Charsets.UTF_8))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            var ivKey: ByteArray? = null
            try {
                ivKey = Cryption.MD5Hash(IV_KEY.toByteArray(Charsets.UTF_8))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            encryptedData = Cryption.encryptAES256(encryptedData, aesKey, ivKey)

            val base64String = Cryption.encodeBase64String(encryptedData!!)

//            Log.i("oh9hPost", "encrypted: $base64String")
            return base64String
        }

    }
}