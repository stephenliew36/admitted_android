package com.realtouchapp.admitted.network.request.user

import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint

/** 用戶類型 */
enum class UserType {
    STUDENT, TEACHER, PARENT;

    val intValue: Int
        get() = when (this) {
            STUDENT -> UserTypeRequest.STUDENT
            TEACHER -> UserTypeRequest.TEACHER
            PARENT -> UserTypeRequest.PARENT
        }

    companion object {
        fun from(value: Int?): UserType? =
            when (value) {
                UserTypeRequest.STUDENT -> STUDENT
                UserTypeRequest.TEACHER -> TEACHER
                UserTypeRequest.PARENT -> PARENT
                else -> null
            }
    }
}

/** 設定用戶類型Request */
data class UserTypeRequest(
    val type: Int
) {
    companion object {
        /** 學生 */
        const val STUDENT = 0
        /** 兼任家教 */
        const val TEACHER = 1
        /** 家長 */
        const val PARENT = 2
    }
}
