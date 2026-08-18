package com.realtouchapp.admitted.storage.database.entity

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    作者: RobertChou
    時間: 2023/3/18 下午 10:26
    信箱: didi31139@gmail.com
    說明:
*/
@Entity(tableName = KeyValueData.TABLE_NAME)
data class KeyValueData(
    @ColumnInfo(name = "_id")
    @PrimaryKey
    var id: Int?,
    var key: String?,
    var keyType: String?,
    var value: String?
){
    companion object {
        const val TABLE_NAME = "KeyValueData"
        const val KEY_TOKEN = "token"
        const val KEY_DEVICETOKEN = "DeviceToken"
        const val KEY_USERINFO = "userInfo"
        const val KEY_USER_TARGETSCHOOLS = "targetSchools"
        const val KEY_USER_APPLICATIONS = "applications"
        const val KEY_USER_TYPE = "userType"
        /** 監護人員才有的學生清單 */
        const val KEY_USER_STUDENTS = "students"
        const val KEY_STUDENT_USERINFO = "studentUserInfo"
        const val KEY_STUDENT_TOKEN = "studentToken"
        const val KEY_STUDENT_ID = "studentID"
        const val KEY_STUDENT_NAME = "studentName"

        const val KEY_ACADEMICYEAR9= "ACADEMICYEAR9"
        const val KEY_ACADEMICYEAR10= "ACADEMICYEAR10"
        const val KEY_ACADEMICYEAR11= "ACADEMICYEAR11"
        const val KEY_ACADEMICYEAR12= "ACADEMICYEAR12"


        const val KEY_LATITUDE= "KEY_LATITUDE"
        const val KEY_LONGITUDE= "KEY_LONGITUDE"

        const val KEY_FIRSTLOGIN= "KEY_FIRSTLOGIN"
    }
}