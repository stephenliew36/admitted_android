package com.realtouchapp.admitted.network.request

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/**
   信箱檢查請求是否已經被使用過
*/
data class RegisterCheckRequest(
    var account: String = "",
//    var countryCode: String? = "",
)
