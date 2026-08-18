package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


data class CommunityImpactRequest(
    var account: String = "",
    var title: String? = "",
    var role: String? = "",
    var name: String? = "",
    var type: String? = "",
    var grade: String? = "",
    var pointType: String? = "",
    var point: String? = "",
    var hoursPerWeek: String? = "",
    var weekPerYear: String? = "",
    var description: String? = "",
)
