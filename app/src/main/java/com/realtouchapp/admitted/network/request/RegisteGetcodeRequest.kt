package com.realtouchapp.admitted.network.request

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


data class RegisteGetcodeRequest(
    var username: String = "",
//    var countryCode: String? = "",
    var method: String = "email"
)
