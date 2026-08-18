package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.entity.Course.Companion.TABLE_NAME

/**@param courseTrack 課程領域 1:English 2:Political Science & Forensics 3:History & Social Studies 4:Mathematics 5:Computer Science & Robotics 6:Science 7:World Languages 8:Classics 9:Performing Arts 10:Visual Arts 11:Health & Physical Education
 * @param courseLevel 課程類型 1:Regular 2:Honors 3:AP 4:IB
 *@param gradeLevel 目前年級  -1:尚未輸入 9:9th grade 10:10th grade 11:11th grade 12:12th grade
 */
@Entity(tableName = TABLE_NAME)
data class Course(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var academicId: Int?,
    var courseLevel: Int?,
    var courseName: String?,
    var courseTrack: Int?,
    var grade: String?,
    var gradeLevel: Int?,
    var gradingScale: Int?,
    @Ignore
    var actionType: Int?=null
) {
    constructor() : this(null, null, null, null, null, null, null, null)

    companion object {
        const val TABLE_NAME = "Course"
    }

    enum class CourseTrack {
        English {
            override fun num() = 34
            override fun title() =R.string.course_track_english_tit
            override fun icon() =R.drawable.vd_ct_english
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_english_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_english)
            }
        },

        HistorySocialStudies {
            override fun num() = 25
            override fun title() =R.string.course_track_history_tit
            override fun icon() =R.drawable.vd_ct_social_studies
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_history_chart,R.string.course_track_history_business_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_history_1,R.drawable.track_history_2)
            }
        },
        PoliticalScienceForensics {
            override fun num() = 20
            override fun title() =R.string.course_track_Political_tit
            override fun icon() =R.drawable.vd_ct_forensics
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_Political_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_political)
            }
        },
        Mathematics {
            override fun num() = 27
            override fun title() =R.string.course_track_math_tit
            override fun icon() =R.drawable.vd_ct_math
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_math_chart,R.string.course_track_math_ib)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_math,R.drawable.track_ib)
            }
        },
        ComputerScienceRobotics {
            override fun num() = 18
            override fun title() =R.string.course_track_computer_tit
            override fun icon() =R.drawable.vd_ct_cs
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_computer_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_computer)
            }
        },
        Science {
            override fun num() = 41
            override fun title() =R.string.course_track_science_tit
            override fun icon() =R.drawable.vd_ct_science
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_science_chart,R.string.course_track_science_scientific_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_sciene,R.drawable.track_sciene_research)
            }
        },
        WorldLanguages {
            override fun num() = 37
            override fun title() =R.string.course_track_worldlan_tit
            override fun icon() =R.drawable.vd_ct_world_languages
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_worldlan_mandarin,R.string.course_track_worldlan_japanese,R.string.course_track_worldlan_spanish)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_world_mandrain,R.drawable.track_worldlan_japanese,R.drawable.track_worldlan_spanish)
            }
        },
        Classics {
            override fun num() = 17
            override fun title() =R.string.course_track_classics_tit
            override fun icon() =R.drawable.vd_ct_classics
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_classics_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_classics)
            }
        },
        PerformingArts {
            override fun num() = 25
            override fun title() =R.string.course_track_performingarts_tit
            override fun icon() =R.drawable.vd_ct_performing_arts
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_performingarts_music_chart,R.string.course_track_performingarts_dance_chart,R.string.course_track_performingarts_theater_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_performing_music,R.drawable.track_performing_dance,R.drawable.track_performing_theater)
            }
        },
        VisualArts {
            override fun num() = 40
            override fun title() =R.string.course_track_visual_tit
            override fun icon() =R.drawable.vd_ct_visual_arts
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_visual_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_visualart)
            }
        },
        HealthPhysicalEducation {
            override fun num() = 11
            override fun title() =R.string.course_track_health_tit
            override fun icon() =R.drawable.vd_ct_pe
            override fun chartId(): List<Int> {
                return  listOf(R.string.course_track_health_chart)
            }

            override fun imageId(): List<Int> {
                return  listOf(R.drawable.track_physical)
            }
        };
        abstract fun num(): Int
        abstract fun title(): Int
        abstract fun icon(): Int
        abstract fun chartId():List<Int>
        abstract fun imageId():List<Int>

    }


}

