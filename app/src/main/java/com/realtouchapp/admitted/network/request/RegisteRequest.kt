package com.realtouchapp.admitted.network.request

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/**註冊請求
 */
data class RegisteRequest(
//    var countryCode: String = "",
//    var cellphone: String = "",
    var email: String = "",
    var checkcode: String = "",
    var password: String = "",
    var password_confirmation: String = "",
    var firstName: String = "",
    var lastName: String = ""
//    var countryCode: String? = "",
):java.io.Serializable
