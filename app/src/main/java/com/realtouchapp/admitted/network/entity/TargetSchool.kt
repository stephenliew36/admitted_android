package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import java.io.Serializable

@Entity(tableName = TargetSchool.TABLE_NAME)
data class TargetSchool(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    val schoolId: Int?,
    val acceptanceRate: Double?,
    val actRequest: String?,
    val actionType: Int?,
    val applicantsInterestLevel: String?,
    val applicationEssay: String?,
    val city: String?,
    val classRank: String?,
    val classSizeBetween20And49: Double?,
    val classSizeFewerThan20: Double?,
    val classSizeMoreThan49: Double?,
    val commonApplicationAccepted: String?,
    val country: Int?,
    val deadlineForAcceptingAdmissionOffers: String?,
    val earlyActionApplicationDeadline: String?,
    val earlyDecisionI: String?,
    val earlyDecisionII: String?,
    val extracurricularActivities: String?,
    val female: Double?,
    val firstDayofSchool: String?,
    val gpaRequest: String?,
    val ieltsRequest: String?,
    val internationalStudents: String?,
    val isLiberalArtsSchools: Int?,
    val isArtSchools: Int?,
    val isPublic: Int?,
    val male: Double?,
    val photos: List<String>?,
    val recommendations: String?,
    val recommendationsLetter: String?,
    val regularDecisionApplicationDeadline: String?,
    val restrictiveEarlyAction: String?,
    val satRequest: String?,
    val schoolName: String?,
    val score: Int?,
    val standardizedTestScores: String?,
    val studentFacultyRatio: String?,
    val toeflRequestInternet: String?,
    val toeflRequestPaper: String?,
    val totalEnrollment: Int?,
    val rank: Int?,
    val tuition: Int?,
    val undergraduateEnrollment: Int?,
    val volunteerWork: String?,
    val websiteAdmission: String?,
    val websiteSchool: String?,
    val workExperience: String?,
    val enable: Int?

): Serializable {
    var isSelected = false
    //上榜難易度
    var difficulty: String = ""
    //演算法的分數
    var scoreAlgorithm: String=""
    //"0":applying    "1":applied    "2":waitlisted    "3":accepted    "4":rejected    "5":enrolled
    var applyingStatus: Int=0

    val isEnabled: Boolean
        get() = enable == 1

    constructor():this(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null, null)
    companion object {
        const val TABLE_NAME = "targetschool"
        //25~49
        const val DIFFICULTY_TARGET ="TARGET"
        //10~24
        const val DIFFICULTY_REACH ="REACH"
        // 0~9
        const val DIFFICULTY_FAR ="FAR"
        //50~
        const val DIFFICULTY_SAFETY ="SAFETY"

        // Far Reach Schools = 名單固定為這 6 所學校 (Harvard/Princeton/Yale/MIT/Caltech/Stanford)，
        // 無論機率或難易度為何，一律歸到 Far Reach（與 web 版/iOS 版相同）
        // 比對需嚴格，避免誤判（例：California Institute of the Arts ≠ Caltech；Harvard-Westlake ≠ Harvard University）
        private val FAR_REACH_SCHOOL_PATTERNS = listOf(
            Regex("""\bharvard\s+university\b""", RegexOption.IGNORE_CASE),
            Regex("""\bharvard\s+college\b""", RegexOption.IGNORE_CASE),
            Regex("""\bprinceton\s+university\b""", RegexOption.IGNORE_CASE),
            Regex("""\byale\s+university\b""", RegexOption.IGNORE_CASE),
            Regex("""\bstanford\s+university\b""", RegexOption.IGNORE_CASE),
            Regex("""\bmassachusetts\s+institute\s+of\s+technology\b""", RegexOption.IGNORE_CASE),
            Regex("""\bcalifornia\s+institute\s+of\s+technology\b""", RegexOption.IGNORE_CASE),
            Regex("""\b(mit|cit|caltech)\b""", RegexOption.IGNORE_CASE)
        )

        fun isFarReachSchool(school: TargetSchool?): Boolean {
            val name = school?.schoolName?.trim() ?: return false
            if (name.isEmpty()) return false
            return FAR_REACH_SCHOOL_PATTERNS.any { it.containsMatchIn(name) }
        }
    }
}