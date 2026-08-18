package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.entity.Test


data class TestListsRequest(
    var tests: ArrayList<Test>
)
