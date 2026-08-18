package com.realtouchapp.oh9hapilibrary.encryption

import android.util.Base64
import java.security.MessageDigest
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/*
  加解密用<br>
  Created by C.C.Chang on 2017/9/29.
*/
class Cryption {

//    login post
//    1. password > MD5Hash > byte2Hex
//    2. 組合json
//    3. json > (encryptAES256(ase,iv)) > encodeBase64String
//    4. post (call,basa64)
//
//    login parse
//    1.
    /** AES256加密  */
    companion object {
        private val kCCKeySizeAES256 = 32
        fun encryptAES256(input: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
            var key = key
            var iv = iv
            if (null == input) {
                return null
            }
            if (null == key) {
                return input
            }

            if (key.size > kCCKeySizeAES256) {
                key = Arrays.copyOf(key, kCCKeySizeAES256)
            } else if (key.size < kCCKeySizeAES256) {
                key = SHA256Hash(key)
            }
            val aesKey = SecretKeySpec(key, "AES")

            var aesIv: AlgorithmParameterSpec? = null
            if (null != iv) {
                if (iv.size > 16) {
                    iv = Arrays.copyOf(iv, 16)
                } else if (iv.size < 16) {
                    iv = MD5Hash(iv)
                }

                aesIv = IvParameterSpec(iv)
            }

            try {
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, aesKey, aesIv)

                return cipher.doFinal(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return input
        }

        /** AES256解密  */
        fun decryptAES256(input: ByteArray?, key: ByteArray?, iv: ByteArray?): ByteArray? {
            var key = key
            var iv = iv
            if (null == input) {
                return null
            }
            if (null == key) {
                return input
            }

            if (key.size > kCCKeySizeAES256) {
                key = Arrays.copyOf(key, kCCKeySizeAES256)
            } else if (key.size < kCCKeySizeAES256) {
                key = SHA256Hash(key)
            }
            val aesKey = SecretKeySpec(key, "AES")

            var aesIv: AlgorithmParameterSpec? = null
            if (null != iv) {
                if (iv.size > 16) {
                    iv = Arrays.copyOf(iv, 16)
                } else if (iv.size < 16) {
                    iv = MD5Hash(iv)
                }

                aesIv = IvParameterSpec(iv)
            }

            try {
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, aesKey, aesIv)

                return cipher.doFinal(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return input
        }

        /** Base64編碼  */
        fun encodeBase64String(input: ByteArray): String {
            return Base64.encodeToString(input, Base64.DEFAULT)
        }

        /** Base64解碼  */
        fun decodeBase64String(inputString: String): ByteArray {
            return Base64.decode(inputString, Base64.DEFAULT)
        }

        /** SHA256雜湊算法  */
        fun SHA256Hash(data: ByteArray): ByteArray {

            val shaCode: MessageDigest
            try {
                shaCode = MessageDigest.getInstance("SHA-256")
                shaCode.update(data)

            } catch (e: Exception) {
                e.printStackTrace()
                return data
            }

            return shaCode.digest()
        }

        /** MD5雜湊算法  */
        fun MD5Hash(data: ByteArray): ByteArray {

            val md5Code: MessageDigest
            try {
                md5Code = MessageDigest.getInstance("MD5")
                md5Code.update(data)

            } catch (e: Exception) {
                e.printStackTrace()
                return data
            }

            return md5Code.digest()
        }


        fun byte2Hex(data: ByteArray): String {
            var hexString = ""

            for (aData in data) {
                hexString += String.format("%02x", aData)
            }

            return hexString
        }
    }

}