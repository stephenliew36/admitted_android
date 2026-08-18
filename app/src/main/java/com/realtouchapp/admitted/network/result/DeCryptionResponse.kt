package com.realtouchapp.admitted.network.result

import com.realtouchapp.admitted.network.entity.*

/**
 * 解密後的回傳*/
data class DeCryptionResponse(
    val honors: List<Honors>,
    val notifications: List<FCMessage>,
    val intellectualCuriosies: List<IntellectualCuriosity>,
    val communityImpacts: List<CommunityImpact>,
    val tests: List<Test>,
    val courses: List<Course>,
    val academicYears: List<AcademicYear>,
    val targetSchools: List<TargetSchool>,
    val targetSchool: List<TargetSchool>,
    val applicationSchools: List<ApplicationSchool>,
    val students: List<Student>,

    val user: UserInfo,

    val updateTime: String?,
    val msgDesc: String,
    val msgTitle: String,
    val success: Int,
    val id: Int,
    val name: String,
    val url: String,
    val agreeTime: String,
    val token: String,
    val data: LoginData?,

    // GET /user/student/{id}
    /** 可使用的訂閱方法 */
    val method: Int?,
    /** 訂閱的產品ID */
    val subscribes: String?,
    /** 學生資料 */
    val student: Student?
)

data class LoginData(
    val name: String?,
    /** 0=學生, 1=兼任家教, 2=家長, 99=尚未選擇 */
    val type: Int?,
    val agreeTime: String?,
    val url: String?
)