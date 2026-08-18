package com.realtouchapp.admitted.network.request

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.UserTargetSchool

/*
    作者: RobertChou
    時間: 2023/3/19 下午 05:48
    信箱: didi31139@gmail.com
    說明:
*/
data class ApplicationSchoolRequest(
    var applicationSchools: ArrayList<ApplicationSchool>?
)
