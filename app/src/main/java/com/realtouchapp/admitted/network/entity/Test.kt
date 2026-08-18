package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


/**
 *@param genre 考試類型 1:SAT 2:ACT 3:TOEFL iBT 4:IELTS 5:AP 6:TOEFL (1-6) 7:IB 8:Duolingo
 * @param score 考試分數，根據不同type,顯示不同的分數級距 SAT (400 ~1600 最小單位10) ACT (0~36)  TOEFL iBT (0~120) IELTS (0.0 ~ 9.0 最小單位0.5)  Ap Exam (1-5)

 * */
@Entity(tableName = Test.TABLE_NAME)
data class Test(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var testId: Int?,
    var apSubject: String?,
    var month: String?,
    var year: Int?,
    var genre: Int?,
    var point: Int?,
    var score: String?,
    var userId: Int?,
    @Ignore
    var actionType: Int?=null
)
{
    constructor():this(null,null,null,null,null,null,null,null,null)
    companion object {
        const val TABLE_NAME = "StandardTest"

        private val MONTHS = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        fun monthIndex(month: String?): Int = MONTHS.indexOf(month).let { if (it < 0) Int.MIN_VALUE else it + 1 }

        fun selectedHighestMostRecent(tests: List<Test>): Test? = tests.maxWithOrNull(
            compareBy<Test> { it.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY }
                .thenBy { it.year ?: Int.MIN_VALUE }
                .thenBy { monthIndex(it.month) }
        )

        /** Keeps one AP attempt per subject: highest score, newest year, then newest month. */
        fun selectedApAttempts(tests: List<Test>): List<Test> =
            selectedSubjectAttempts(tests, GenreInfo.AP)

        /** Keeps one IB attempt per subject: highest score, newest year, then newest month. */
        fun selectedIbAttempts(tests: List<Test>): List<Test> =
            selectedSubjectAttempts(tests, GenreInfo.IB)

        fun selectedSubjectAttempts(tests: List<Test>, genre: GenreInfo): List<Test> = tests
            .filter { it.genre == genre.value() }
            .groupBy { it.apSubject.orEmpty() }
            .values
            .mapNotNull(::selectedHighestMostRecent)

        fun sortedChronologically(tests: List<Test>): List<Test> = tests.sortedWith(
            compareBy<Test> { it.year ?: Int.MIN_VALUE }.thenBy { monthIndex(it.month) }
        )

        /** Mirrors iOS `conFigTestGroupArray`: SAT/ACT/language genres together, AP and IB grouped by subject. */
        fun groupedAttempts(tests: List<Test>): List<List<Test>> {
            val groups = mutableListOf<List<Test>>()
            listOf(
                GenreInfo.SAT, GenreInfo.ACT, GenreInfo.TOEFL, GenreInfo.IELTS,
                GenreInfo.TOEFL_ALTERNATE, GenreInfo.DUOLINGO
            ).forEach { info ->
                val attempts = sortedChronologically(tests.filter { it.genre == info.value() })
                if (attempts.isNotEmpty()) groups.add(attempts)
            }
            listOf(GenreInfo.AP, GenreInfo.IB).forEach { info ->
                tests.filter { it.genre == info.value() }
                    .groupBy { it.apSubject.orEmpty() }
                    .values
                    .forEach { attempts -> groups.add(sortedChronologically(attempts)) }
            }
            return groups
        }
    }

    enum class GenreInfo {
        SAT {
            override fun value() = 1
            override fun score_MIN() = 400f
            override fun score_MAX() = 1600f
            override fun score_hint() = "${score_MIN()} ~ ${score_MAX()}"
            override fun title() = "SAT"

            override fun score_unit() = 10f
        },
        ACT {
            override fun value() = 2
            override fun score_MIN() = 0f
            override fun score_MAX() = 36f
            override fun score_hint() = "${score_MIN()} ~ ${score_MAX()}"
            override fun title() = "ACT"
            override fun score_unit() = 1f
        },
        TOEFL {
            override fun value() = 3
            override fun score_MIN() = 0f
            override fun score_MAX() = 120f
            override fun score_hint() = "${score_MIN()} ~ ${score_MAX()}"
            override fun title() = "TOEFL iBT"
            override fun score_unit() = 1f
        },
        IELTS {
            override fun value() = 4
            override fun score_MIN() = 0.0f
            override fun score_MAX() = 9.0f
            override fun score_hint() = "${score_MIN()} ~ ${score_MAX()}"
            override fun title() = "IELTS"
            override fun score_unit() = 0.5f
        },
        AP {
            override fun value() = 5
            override fun score_MIN() = 1f
            override fun score_MAX() = 5f
            override fun score_hint() = "${score_MIN()} ~ ${score_MAX()}"
            override fun title() = "AP Exam"
            override fun score_unit() = 1f
        },
        TOEFL_ALTERNATE {
            override fun value() = 6
            override fun score_MIN() = 1f
            override fun score_MAX() = 6f
            override fun score_hint() = "1.0 ~ 6.0"
            override fun title() = "TOEFL (1 - 6)"
            override fun score_unit() = 0.5f
        },
        IB {
            override fun value() = 7
            override fun score_MIN() = 1f
            override fun score_MAX() = 7f
            override fun score_hint() = "1 ~ 7"
            override fun title() = "IB"
            override fun score_unit() = 1f
        },
        DUOLINGO {
            override fun value() = 8
            override fun score_MIN() = 10f
            override fun score_MAX() = 160f
            override fun score_hint() = "10 ~ 160"
            override fun title() = "Duolingo English Test"
            override fun score_unit() = 5f
        };
        abstract fun value(): Int
        abstract fun score_MIN(): Float
        abstract fun score_MAX(): Float
        abstract fun title(): String
        abstract fun score_hint(): String
        abstract fun score_unit(): Float

        fun isValidScore(text: String?): Boolean {
            val value = text?.toFloatOrNull() ?: return false
            if (value < score_MIN() || value > score_MAX()) return false
            val steps = (value - score_MIN()) / score_unit()
            return kotlin.math.abs(steps - kotlin.math.round(steps)) < 0.0001f
        }

        fun requiresSubject(): Boolean = this == AP || this == IB

        fun scoreSuffix(): String = when (this) {
            SAT -> "/ 1600"
            ACT -> "/ 36"
            TOEFL -> "/ 120"
            IELTS -> "/ 9.0"
            AP -> "/ 5"
            TOEFL_ALTERNATE -> "/ 6"
            IB -> "/ 7"
            DUOLINGO -> "/ 160"
        }

        companion object {
            fun fromValue(value: Int?): GenreInfo? = entries.firstOrNull { it.value() == value }
        }
    }

    fun genreInfo(): GenreInfo {

        return GenreInfo.fromValue(genre) ?: GenreInfo.SAT
    }

    fun title(): String {

        val info = GenreInfo.fromValue(genre) ?: return ""
        return if (info.requiresSubject()) apSubject.takeUnless { it.isNullOrBlank() } ?: info.title() else info.title()
    }

    fun displayScore(): String = score?.let { "$it ${genreInfo().scoreSuffix()}" } ?: ""

    fun requiresSubject(): Boolean = GenreInfo.fromValue(genre)?.requiresSubject() == true
}
