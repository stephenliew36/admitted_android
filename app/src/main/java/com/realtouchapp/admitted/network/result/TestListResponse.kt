package com.realtouchapp.admitted.network.result

import com.realtouchapp.admitted.network.entity.Test

data class TestListResponse(
    val failed: ArrayList<Any>,
    val msgDesc: String,
    val msgTitle: String,
    val success: ArrayList<Test>
)