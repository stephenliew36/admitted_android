package com.realtouchapp.admitted.network.request.user

import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


/**@param courseTrack 課程領域 1:English 2:Political Science & Forensics 3:History & Social Studies 4:Mathematics 5:Computer Science & Robotics 6:Science 7:World Languages 8:Classics 9:Performing Arts 10:Visual Arts 11:Health & Physical Education
 * @param courseLevel 課程類型 1:Regular 2:Honors 3:AP 4:IB
 *@param gradeLevel 目前年級  -1:尚未輸入 9:9th grade 10:10th grade 11:11th grade 12:12th grade
 */
data class AcademicRequest(
    var courseLevel: Int,
    var courseName: String,
    var courseTrack: Int,
    var grade: String,
    var gradeLevel: Int,
    var gradingScale: Int
)
