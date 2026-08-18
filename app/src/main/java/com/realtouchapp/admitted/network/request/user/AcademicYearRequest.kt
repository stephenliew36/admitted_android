package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/**
// *@param gradeSystem 學年總成績計分方式 1:IB (1 - 7)  2:Weighted GPA (0.0 - 5.0) 3:Unweighted GPA (0.0 - 4.0)  4:Percentage (0 - 100%)
 *@param grade GAP分數 0.0 - 5.0
 * */
data class AcademicYearRequest(
    var grade: String,
    var gradeSystem: String="2"
)
