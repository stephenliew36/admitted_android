package com.realtouchapp.admitted.network.entity

import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/*
    作者: RobertChou
    時間: 2023/3/19 上午 01:26
    信箱: didi31139@gmail.com
    說明:
*/
data class ApplicationSchool(
    var schoolId: Int?,
    var applyingStatus: Int = 0,
    var isAcademicCalendar: String? = "0",
    var isBankAccount: String? = "0",
    var isBoardingCustomscheck: String? = "0",
    var isCourseSelection: String? = "0",
    var isEnteringTheUs: String? = "0",
    var isHousing: String? = "0",
    var isMedicalExamination: String? = "0",
    var isMobilePlan: String? = "0",
    var isNewStudentOrientation: String? = "0",
    var isStudentVisa: String? = "0",
    var isStudyPermit: String? = "0",
    var isTuitionFees: String? = "0",
    var isVaccinationHealth: String? = "0",
    var isVaccinationsTBTestandNHS: String? = "0",
    var essay: List<Essay>?
) {
    constructor(schoolID: Int?, applyingStatus: Int) :this(schoolID,applyingStatus,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
}
