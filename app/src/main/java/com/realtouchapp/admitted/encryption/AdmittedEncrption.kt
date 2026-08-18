package com.realtouchapp.admitted.encryption

import android.content.Context
import com.realtouchapp.oh9hapilibrary.encryption.EnCryption

/*
    作者: RobertChou
    時間: 2023/2/1 上午 10:07
    信箱: didi31139@gmail.com
    說明:
*/
class AdmittedEncrption {

    companion object {
        //正式版
        const val AES_KEY_LOGIN = "ff7684474eeb1232fb2e17acde9c15ff"
        const val IV_KEY = "RealTouchAppInTW"
        //測試版
//        const val AES_KEY_LOGIN = "ff7684474eeb1232fb2e17acde9c15ff"
//        const val IV_KEY = "RealTouchAppInTW"
    }

    //使用當前的 activityKey 加密
    fun enCryptionCurrentLogin(context: Context, jsonString: String): String {

        return EnCryption.enCryption(
            jsonString,
            AES_KEY_LOGIN,
//            JoinEventSharePreference.getString(context, JoinEventSharePreference.Key.KEY_AESKEY,"")!!,
            IV_KEY
        )
    }

    //使用當前的 activityKey 解密
    fun deCryptionCurrentLogin(context: Context, jsonString: String): String {
        return EnCryption.deCryption(
            jsonString,
            AES_KEY_LOGIN,
//            JoinEventSharePreference.getString(context, JoinEventSharePreference.Key.KEY_AESKEY,"")!!,
            IV_KEY
        )
    }

    fun enCryption(jsonString: String): String {
        return EnCryption.enCryption(jsonString, AES_KEY_LOGIN, IV_KEY)
    }

    fun deCryption(jsonString: String): String {
        return EnCryption.deCryption(jsonString, AES_KEY_LOGIN, IV_KEY)
    }

    fun enCryption(jsonString: String, AES_KEY: String): String {
        return EnCryption.enCryption(jsonString, AES_KEY, IV_KEY)
    }
}