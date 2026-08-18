package com.realtouchapp.admitted.network.entity

import java.io.Serializable

/** 學生資料 */
data class Student(
    val systemNumber: String,
    val lastName: String? = null,
    val firstName: String? = null,
    val email: String? = null,
    val nationality: String? = null,
    val school: String? = null,
    val schoolSystem: Int? = null,
    val schedule: Int? = null,
    val score: Int? = null,
    val grade: Int? = null,
    val isRequireLanguageExam: String? = null,
    val gradingScale: Int? = null,
    val token: String = "",
    val photo: String? = null,
    val name: String? = null
): Serializable

data class StudentSaveData(
    val students: List<Student>
)