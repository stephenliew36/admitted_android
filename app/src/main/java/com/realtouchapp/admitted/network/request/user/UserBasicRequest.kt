package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/**
 *@param schoolSystem 課程系統 -1:尚未輸入 1:US System 2:IBD(International Baccalaureate Diploma Programme )
 * @param schedule 年級學制 -1:尚未輸入 1:Temesters 2:Trimesters 3:Quarters 4:Yearly 5:Other
 *@param grade 目前年級  -1:尚未輸入 9:9th grade 10:10th grade 11:11th grade 12:12th grade
 *@param gradingScale 科目分級量表 1:A-F 2:1-100 3:1-7 4:Others ( Please use percentage when prompted to enter your score)
 * #

 */

data class UserBasicRequest(
    var lastName: String ?=null,
    var firstName: String ?=null,
    var nationality: String ?=null,
    var school: String ?=null,
    var schoolSystem: Int?=null,
    var schedule: Int?=null,
    var score: String = "",
    var grade: Int?=null,
    var gradingScale: Int?=null,
    var areaOfInterest: AreaOfInterest? =null
)


//興趣領域
data class AreaOfInterest(
    //1選中 0不選
    var Math: String = "",
    var Life_Sciences: String = "",
    var Engineering: String = "",
    var Social_Sciences: String = "",
    var Literature: String = "",
    var Business: String = "",
    var Art: String = ""
)