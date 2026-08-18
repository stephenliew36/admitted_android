package com.realtouchapp.admitted.network.entity

import android.content.Context
import com.realtouchapp.admitted.R

data class AreaOfInterest(
    var Math: String = "0",
    var Art: String = "0",
    var Business: String = "0",
    var Engineering: String = "0",
    var Life_Sciences: String = "0",
    var Literature: String = "0",
    var Social_Sciences: String = "0"
) {
    fun getCheckedList(context: Context):ArrayList<String>{
        var list = ArrayList<String>()
        if (isChecked(R.string.interest_math)){
            list.add(context.getString(R.string.interest_math))
        }
        if (isChecked(R.string.interest_art)){
            list.add(context.getString(R.string.interest_art))
        }
        if (isChecked(R.string.interest_business)){
            list.add(context.getString(R.string.interest_business))
        }
        if (isChecked(R.string.interest_engineering)){
            list.add(context.getString(R.string.interest_engineering))
        }
        if (isChecked(R.string.interest_life_sciences)){
            list.add(context.getString(R.string.interest_life_sciences))
        }
        if (isChecked(R.string.interest_literature)){
            list.add(context.getString(R.string.interest_literature))
        }
        if (isChecked(R.string.interest_socia_sciencesl)){
            list.add(context.getString(R.string.interest_socia_sciencesl))
        }
        return list

    }

    fun setChecked(isChecked: Boolean, titleId: Int) {
        var stringValue = "0"
        if (isChecked) {
            stringValue = "1"
        }
        when (titleId) {
            R.string.interest_math -> {
                Math = stringValue
            }
            R.string.interest_art -> {
                Art = stringValue
            }
            R.string.interest_business -> {
                Business = stringValue
            }
            R.string.interest_engineering -> {
                Engineering = stringValue
            }
            R.string.interest_life_sciences -> {
                Life_Sciences = stringValue
            }
            R.string.interest_literature -> {
                Literature = stringValue
            }
            R.string.interest_socia_sciencesl -> {
                Social_Sciences = stringValue
            }
        }
    }

    fun isChecked(titleId: Int): Boolean {
        var isChecked = false
        when (titleId) {
            R.string.interest_math -> {
                isChecked = !Math.equals("0")
            }
            R.string.interest_art -> {
                isChecked = !Art.equals("0")
            }
            R.string.interest_business -> {
                isChecked = !Business.equals("0")
            }
            R.string.interest_engineering -> {
                isChecked = !Engineering.equals("0")
            }
            R.string.interest_life_sciences -> {
                isChecked = !Life_Sciences.equals("0")
            }
            R.string.interest_literature -> {
                isChecked = !Literature.equals("0")
            }
            R.string.interest_socia_sciencesl -> {
                isChecked = !Social_Sciences.equals("0")
            }
        }
        return isChecked
    }

    fun setChecked(context: Context,isChecked: Boolean, title: String) {
        var stringValue = "0"
        if (isChecked) {
            stringValue = "1"
        }

        when (title) {
            context.getString(R.string.interest_math) -> {
                Math = stringValue
            }
            context.getString(R.string.interest_art) -> {
                Art = stringValue
            }
            context.getString(R.string.interest_business) -> {
                Business = stringValue
            }
            context.getString(R.string.interest_engineering) -> {
                Engineering = stringValue
            }
            context.getString(R.string.interest_life_sciences) -> {
                Life_Sciences = stringValue
            }
            context.getString(R.string.interest_literature) -> {
                Literature = stringValue
            }
            context.getString(R.string.interest_socia_sciencesl) -> {
                Social_Sciences = stringValue
            }
        }
    }

    fun isChecked(context: Context,title: String): Boolean {
        var isChecked = false
        when (title) {
            context.getString(R.string.interest_math) -> {
                isChecked = !Math.equals("0")
            }
            context.getString(R.string.interest_art) -> {
                isChecked = !Art.equals("0")
            }
            context.getString(R.string.interest_business) -> {
                isChecked = !Business.equals("0")
            }
            context.getString(R.string.interest_engineering) -> {
                isChecked = !Engineering.equals("0")
            }
            context.getString(R.string.interest_life_sciences) -> {
                isChecked = !Life_Sciences.equals("0")
            }
            context.getString(R.string.interest_literature) -> {
                isChecked = !Literature.equals("0")
            }
            context.getString(R.string.interest_socia_sciencesl) -> {
                isChecked = !Social_Sciences.equals("0")
            }
        }
        return isChecked
    }
    companion object{
        //取得該興趣含有的CourseTrack
        fun getCourseTrack(context: Context,title:String):ArrayList<Course.CourseTrack>{
            var courseTracks = ArrayList<Course.CourseTrack>()
            when (title) {
                context.getString(R.string.interest_math) -> {
                    courseTracks.add(Course.CourseTrack.Mathematics)
                    courseTracks.add(Course.CourseTrack.ComputerScienceRobotics)
                    courseTracks.add(Course.CourseTrack.Science)

                }
                context.getString(R.string.interest_art) -> {
                    courseTracks.add(Course.CourseTrack.PerformingArts)
                    courseTracks.add(Course.CourseTrack.VisualArts)
                    courseTracks.add(Course.CourseTrack.HealthPhysicalEducation)
                }
                context.getString(R.string.interest_business) -> {
                    courseTracks.add(Course.CourseTrack.English)
                    courseTracks.add(Course.CourseTrack.Mathematics)
                    courseTracks.add(Course.CourseTrack.HistorySocialStudies)
                }
                context.getString(R.string.interest_engineering) -> {
                    courseTracks.add(Course.CourseTrack.Mathematics)
                    courseTracks.add(Course.CourseTrack.ComputerScienceRobotics)
                    courseTracks.add(Course.CourseTrack.Science)
                }
                context.getString(R.string.interest_life_sciences) -> {
                    courseTracks.add(Course.CourseTrack.Mathematics)
                    courseTracks.add(Course.CourseTrack.Science)
                    courseTracks.add(Course.CourseTrack.HealthPhysicalEducation)
                }
                context.getString(R.string.interest_literature) -> {
                    courseTracks.add(Course.CourseTrack.English)
                    courseTracks.add(Course.CourseTrack.HistorySocialStudies)
                    courseTracks.add(Course.CourseTrack.WorldLanguages)
                    courseTracks.add(Course.CourseTrack.Classics)
                }
                context.getString(R.string.interest_socia_sciencesl) -> {
                    courseTracks.add(Course.CourseTrack.English)
                    courseTracks.add(Course.CourseTrack.WorldLanguages)
                    courseTracks.add(Course.CourseTrack.HistorySocialStudies)
                }
            }

            return courseTracks

        }
}

}

