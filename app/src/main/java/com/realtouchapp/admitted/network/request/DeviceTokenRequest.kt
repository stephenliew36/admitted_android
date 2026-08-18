package com.realtouchapp.admitted.network.request

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/**註冊請求
 */
data class DeviceTokenRequest(
    var deviceToken: String = "",
    val type: String = "Android"
): java.io.Serializable
