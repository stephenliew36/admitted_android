package com.realtouchapp.admitted.storage.database.dao

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.ApplicationSchoolRequest
import com.realtouchapp.admitted.network.request.UserTargetSchoolRequest
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import com.realtouchapp.admitted.storage.database.entity.KeyValueData
import java.util.stream.Collectors

@Dao
interface KeyValueDao {
    @Query("SELECT * FROM ${KeyValueData.TABLE_NAME} WHERE `key` = :key LIMIT 1")
    fun getValueByKey(key: String): KeyValueData?

    @Transaction
    fun saveKeyValue(keyValueData: KeyValueData) {
        val data = getValueByKey(keyValueData.key!!)
        if (data == null) {
            insertKeyValueData(keyValueData)
        } else {
            keyValueData.id = data.id
            insertKeyValueData(keyValueData)
        }
    }


    //儲存單個
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeyValueData(data: KeyValueData)

    //刪除多個
    @Query("DELETE from ${KeyValueData.TABLE_NAME}")
    fun deleteAllKeyValueData()

    @Delete
    fun deleteKeyValueData(tests: KeyValueData)

    @Delete
    fun deleteKeyValueData(datas: List<KeyValueData>)


    fun saveUserInfo(userInfo: UserInfo) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_USERINFO, "", Gson().toJson(userInfo)))
    }

    fun getUserInfo(): UserInfo? {
        val json = getValueByKey(KeyValueData.KEY_USERINFO)

        if (json != null) {
            return Gson().fromJson(json.value, UserInfo::class.java)
        }
        return null
    }

    fun saveStudentUserInfo(userInfo: UserInfo) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_STUDENT_USERINFO, "", Gson().toJson(userInfo)))
    }

    fun getStudentUserInfo(): UserInfo? {
        val json = getValueByKey(KeyValueData.KEY_STUDENT_USERINFO)

        if (json != null) {
            return Gson().fromJson(json.value, UserInfo::class.java)
        }
        return null
    }

    fun saveUserType(userType: Int) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_USER_TYPE, "", "$userType"))
    }

    fun getUserType(): Int? {
        return getValueByKey(KeyValueData.KEY_USER_TYPE)?.let { json ->
            try {
                json.value?.toInt()
            } catch (_: Exception) {
                null
            }
        }
    }

    fun saveToken(value: String) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_TOKEN, "", value))
    }

    fun setLogined() {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_FIRSTLOGIN, "", "1"))
    }
    fun isLogined(): Boolean {
        val isLogined = getValueByKey(KeyValueData.KEY_FIRSTLOGIN)
        if (isLogined != null) {
            return true
        }else{
            return  false
        }
    }

    fun getToken(): String? {
        val json = getValueByKey(KeyValueData.KEY_TOKEN)

        if (json != null) {
            return json.value
        }
        return null
    }

    /** 「家長」選擇的學生token, systemNumber和名稱 */
    fun saveStudentTokenAndId(token: String, id: String, name: String) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_STUDENT_TOKEN, "", token))
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_STUDENT_ID, "", id))
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_STUDENT_NAME, "", name))
    }

    /** 「家長」選擇的學生Token */
    fun getStudentToken(): String? {
        val json = getValueByKey(KeyValueData.KEY_STUDENT_TOKEN)

        if (json != null) {
            return json.value
        }
        return null
    }

    /** 「家長」選擇的學生Id */
    fun getStudentId(): String? {
        val json = getValueByKey(KeyValueData.KEY_STUDENT_ID)

        if (json != null) {
            return json.value
        }
        return null
    }

    /** 「家長」選擇的學生名稱 */
    fun getStudentName(): String? {
        val json = getValueByKey(KeyValueData.KEY_STUDENT_NAME)

        if (json != null) {
            return json.value
        }
        return null
    }

    /** 「家長」選擇的學生Token */
    fun clearStudentTokenAndId() {
        getValueByKey(KeyValueData.KEY_STUDENT_TOKEN)
            ?.let {
                deleteKeyValueData(it)
            }
        getValueByKey(KeyValueData.KEY_STUDENT_ID)
            ?.let {
                deleteKeyValueData(it)
            }
        getValueByKey(KeyValueData.KEY_STUDENT_NAME)
            ?.let {
                deleteKeyValueData(it)
            }
    }

    fun saveDeviceToken(value: String) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_DEVICETOKEN, "", value))
    }

    fun getDeviceToken(): String? {
        val json = getValueByKey(KeyValueData.KEY_DEVICETOKEN)

        if (json != null) {
            return json.value
        }
        return null
    }
    fun saveLocation(Location: GooglePlaceNearby.Location) {
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_LATITUDE, "", Location.lat.toString()))
        saveKeyValue(KeyValueData(null, KeyValueData.KEY_LONGITUDE, "", Location.lng.toString()))
    }
    fun getLocation():GooglePlaceNearby.Location?{
        val lat = getValueByKey(KeyValueData.KEY_LATITUDE)
        val lon = getValueByKey(KeyValueData.KEY_LONGITUDE)
        if (lat != null && lon != null){
            return GooglePlaceNearby.Location(lat.value!!.toDouble(),lon.value!!.toDouble())
        }else{
            return null
        }
    }

    fun deleteUserTargetSchool(targetSchool: UserTargetSchool){
        val targetSchools = getUserTargetschools()
        if (targetSchools != null){
            val result = targetSchools
                .stream()
                .filter { it.schoolId!=targetSchool.schoolId }
                .collect(Collectors.toList()) as ArrayList<UserTargetSchool>
            saveUserTargetschools(result)
        }
    }
    fun saveUserTargetschool(targetSchool: UserTargetSchool) {
        var targetSchools = getUserTargetschools()
        if (targetSchools == null){
            targetSchools = ArrayList<UserTargetSchool>()
            targetSchools.add(targetSchool)
            saveUserTargetschools(targetSchools)
        }else{
            targetSchools.add(targetSchool)
            saveUserTargetschools(targetSchools)
        }
    }
    fun saveUserTargetschools(targetSchools: ArrayList<UserTargetSchool>) {
        saveKeyValue(
            KeyValueData(
                null,
                KeyValueData.KEY_USER_TARGETSCHOOLS,
                "",
                Gson().toJson(UserTargetSchoolRequest(targetSchools))
            )
        )
    }
    fun getUserTargetschools(): ArrayList<UserTargetSchool>? {
        val json = getValueByKey(KeyValueData.KEY_USER_TARGETSCHOOLS)

        if (json != null) {
            val datas = Gson().fromJson(json.value, UserTargetSchoolRequest::class.java)
            return datas.targetSchools
        }
        return null
    }

    fun deleteApplicationSchool(applicationSchool: ApplicationSchool){
        val applicationSchools = getApplicationSchools()
        if (applicationSchools != null){
            val result = applicationSchools
                .stream()
                .filter { it.schoolId!=applicationSchool.schoolId }
                .collect(Collectors.toList()) as ArrayList<ApplicationSchool>
            saveApplicationSchools(result)
        }
    }
    fun saveApplicationSchool(applicationSchool: ApplicationSchool) {
        var applicationSchools = getApplicationSchools()
        if (applicationSchools == null){
            applicationSchools = ArrayList<ApplicationSchool>()
            applicationSchools.add(applicationSchool)
            saveApplicationSchools(applicationSchools)
        }else{
            applicationSchools.add(applicationSchool)
            saveApplicationSchools(applicationSchools)
        }
    }
    fun saveApplicationSchools(applicationSchools: ArrayList<ApplicationSchool>) {

        saveKeyValue(
            KeyValueData(
                null,
                KeyValueData.KEY_USER_APPLICATIONS,
                "",
                Gson().toJson(ApplicationSchoolRequest(applicationSchools))
            )
        )
    }
    fun getApplicationSchools(): ArrayList<ApplicationSchool>? {
        val json = getValueByKey(KeyValueData.KEY_USER_APPLICATIONS)

        if (json != null) {
            val datas = Gson().fromJson(json.value, ApplicationSchoolRequest::class.java)
            return datas.applicationSchools
        }
        return null
    }


    fun saveAcademicYear(academicYear: AcademicYear) {
        if (academicYear.academicYearId.isEmpty()) {
            return
        }
        when (academicYear.academicYearId) {
            "9" -> {
                saveKeyValue(
                    KeyValueData(
                        null,
                        KeyValueData.KEY_ACADEMICYEAR9,
                        "",
                        academicYear.grade
                    )
                )
            }
            "10" -> {
                saveKeyValue(
                    KeyValueData(
                        null,
                        KeyValueData.KEY_ACADEMICYEAR10,
                        "",
                        academicYear.grade
                    )
                )
            }
            "11" -> {
                saveKeyValue(
                    KeyValueData(
                        null,
                        KeyValueData.KEY_ACADEMICYEAR11,
                        "",
                        academicYear.grade
                    )
                )
            }
            "12" -> {
                saveKeyValue(
                    KeyValueData(
                        null,
                        KeyValueData.KEY_ACADEMICYEAR12,
                        "",
                        academicYear.grade
                    )
                )
            }
        }

    }
    fun saveAcademicYears(response: DeCryptionResponse) {
        for (item in response.academicYears) {
            saveAcademicYear(item)
        }
    }
    fun getAcademicYear(grade: Int): AcademicYear {
        val academicYear = AcademicYear(grade.toString(), "0.0", "2")
        when (grade.toString()) {
            "9" -> {
                val score = getValueByKey(KeyValueData.KEY_ACADEMICYEAR9)?.value ?: "0.0"
                academicYear.grade = score
            }
            "10" -> {
                val score = getValueByKey(KeyValueData.KEY_ACADEMICYEAR10)?.value ?: "0.0"
                academicYear.grade = score
            }
            "11" -> {
                val score = getValueByKey(KeyValueData.KEY_ACADEMICYEAR11)?.value ?: "0.0"
                academicYear.grade = score
            }
            "12" -> {
                val score = getValueByKey(KeyValueData.KEY_ACADEMICYEAR12)?.value ?: "0.0"
                academicYear.grade = score
            }
        }
        return academicYear
    }
    fun getAcademicYears():ArrayList<AcademicYear> {
        val academicYears = ArrayList<AcademicYear>()
        val academicYear9 = getAcademicYear(9)
        academicYears.add(academicYear9)
        val academicYear10 = getAcademicYear(10)
        academicYears.add(academicYear10)
        val academicYear11 = getAcademicYear(11)
        academicYears.add(academicYear11)
        val academicYear12 = getAcademicYear(12)
        academicYears.add(academicYear12)

        return academicYears
    }

    fun addStudent(student: Student) {
        val students = ArrayList<Student>(
            getStudents() ?: emptyList()
        ).apply {
            add(student)
        }.toList()

        saveStudents(students)
    }

    fun deleteStudent(student: Student) {
        val students = ArrayList<Student>(
            getStudents() ?: emptyList()
        ).apply {
            removeIf { it.systemNumber == student.systemNumber }
        }.toList()


        saveStudents(students)
    }

    fun saveStudents(students: List<Student>) {
        saveKeyValue(
            KeyValueData(
                null,
                KeyValueData.KEY_USER_STUDENTS,
                "",
                Gson().toJson(StudentSaveData(students))
            )
        )
    }

    fun getStudents(): List<Student>? {
        val json = getValueByKey(KeyValueData.KEY_USER_STUDENTS)

        if (json != null) {
            val data = Gson().fromJson(json.value, StudentSaveData::class.java)
            return data.students
        }
        return null
    }

    fun clearStudents() {
        getValueByKey(KeyValueData.KEY_USER_STUDENTS)
            ?.let {
                deleteKeyValueData(it)
            }
    }
}

//    //有條件刪除多個
//    @Query("DELETE FROM ${KeyValueData.TABLE_NAME} WHERE ${DaoKey.APPLICATIONID} = :applicationID")
//    fun deleteAllByApplicationID(applicationID:String)
