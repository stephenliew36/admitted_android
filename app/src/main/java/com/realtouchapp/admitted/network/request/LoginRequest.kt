package com.realtouchapp.admitted.network.request

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/*
    作者: RobertChou
    時間: 2023/2/3 上午 12:35
    信箱: didi31139@gmail.com
    說明:
*/
data class LoginRequest(
    var email: String = "",
    var password: String = "",
) {

    fun toJsonEncode():String{
        //全部編譯的
        testPrint(this.javaClass.simpleName, Gson().toJson(this))
        var encode = AdmittedEncrption().enCryption(Gson().toJson(this))
        testPrint(this.javaClass.simpleName+" Encode:", encode)
        return  encode
    }

}