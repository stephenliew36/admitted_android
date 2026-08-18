package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


data class IntellectualCuriosityRequest(
    var account: String = "",
    var countryCode: String? = "",
)
