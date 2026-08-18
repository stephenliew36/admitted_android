package com.realtouchapp.admitted.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.testPrint


/**
 * @param grade  目前年級  -1:尚未輸入 9:9th grade 10:10th grade 11:11th grade 12:12th grade
 *
 * */
@Entity(tableName = UserInfo.TABLE_NAME)
data class UserInfo(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var id: Int=1,
    var firstName: String?=null,
    var grade: Int?=null,
    var gradingScale: Int?=null,
    var email: String?=null,
    var lastName: String?=null,
    var nationality: String?=null,
    var schedule: Int?=null,
    var school: String?=null,
    var schoolSystem: Int?=null,
    var isRequireLanguageExam: String?=null,
    var isSubscribed: String?=null,
    var expirationDate: String?=null,
    var score: String?=null,
    /**  */
    var subscriptionType: String?=null,

    /** 2024.08.06: 個人資料增加回傳systemNumber */
    var systemNumber: String? = null,


//    var areaOfInterestJson :String?=null,
//    @Ignore
    var areaOfInterest :AreaOfInterest?=null,

    @Ignore
    var students: List<Student>? = null,
    @Ignore
    var student: Student? = null
){
    constructor():this(1,null,null,null,null,null,null,1,null,null,null,null)
    companion object {
        const val TABLE_NAME = "UserInfo"
    }
}