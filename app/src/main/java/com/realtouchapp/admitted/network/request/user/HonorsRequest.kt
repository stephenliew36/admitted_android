package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


data class HonorsRequest(
    var grade: Int,
    var level: Int,
    var point: Int,
    var pointType: Int,
    var rank: String,
    var researchType: String,
    var title: String,
    var type: String
)
