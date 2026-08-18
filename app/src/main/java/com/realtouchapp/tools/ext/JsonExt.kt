package com.realtouchapp.tools.ext

import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

fun Any.toJsonEncode(): String {
    //全部編譯的
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    testPrint(this.javaClass.simpleName, gson.toJson(this))
    val encode = AdmittedEncrption().enCryption(gson.toJson(this))
    testPrint("${this.javaClass.simpleName} Encode:", encode)
    return encode
}