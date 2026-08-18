package com.realtouchapp.admitted.network.api

import android.util.Log
import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.DeviceTokenRequest
import com.realtouchapp.admitted.network.request.user.PostUserStudentRequest
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.network.result.base.NetworkResult.Action
import com.realtouchapp.admitted.storage.database.dao.*
import com.realtouchapp.admitted.storage.datastore.UpdateTimeStore
import com.realtouchapp.tools.ext.toJsonEncode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.runBlocking
import java.util.stream.Collectors
import javax.inject.Inject


/*
    取得資料，會同時快取取得資料庫資料和網路上的資料顯示，取到最新資料也會一併存入資料庫刷新
*/

class AdmittedReposritory @Inject constructor(
    private val admittedRetrofitRemote:AdmittedRetrofitRemote,
    private val updateTimeStore: UpdateTimeStore,
    private val honorsDao: HonorsDao,
    private val testDao: TestDao,
    private val courseDao: CourseDao,
    val extracurricularDao: ExtracurricularDao,
    val targetSchoolDao: TargetSchoolDao,
    private val fcMessageDao: FCMessageDao,
    val keyValueDao: KeyValueDao,
    private val communityImpactDao: CommunityImpactDao,
    private val intellectualCuriosityDao: IntellectualCuriosityDao
) {

    fun deleteAllDao() {
        keyValueDao.deleteAllKeyValueData()
        honorsDao.deleteAllHonors()
        testDao.deleteAllTest()
        courseDao.deleteAllCourse()
        targetSchoolDao.deleteAllTargetSchool()
        fcMessageDao.deleteAllFCMessage()
        communityImpactDao.deleteAllCommunityImpact()
        intellectualCuriosityDao.deleteAllIntellectualCuriosity()
    }

    val token: String?
        get() {
            return when (UserType.from(keyValueDao.getUserType())) {
                UserType.TEACHER, UserType.PARENT ->
                    keyValueDao.getStudentToken()
                else ->
                    keyValueDao.getToken()
            }
        }

    suspend fun postHonors(honors: Honors): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_HONORS))
            token?.let {
                val result = admittedRetrofitRemote.postHonors(it,honors)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java)
                        honors.id =response.id
                        honorsDao.insertHonors(honors)
                    }

                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putHonors(honors: Honors): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_HONORS))
            token?.let {
                val result = admittedRetrofitRemote.putHonors(it,honors)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    honorsDao.insertHonors(honors)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteHonors(honors: Honors): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_HONORS))

            token?.let {
                val result = admittedRetrofitRemote.deleteHonors(it,honors)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    honorsDao.deleteHonors(honors)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFlowHonorsCached(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getHonorsCached()?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }
    private fun getHonorsCached(): NetworkResult<CommonResponse>? = honorsDao.getAll()?.let {
        NetworkResult.success(Action.CACHE_HONORS,CommonResponse(data = it), 0)
    }

    suspend fun getHonors(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getHonorsCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_HONORS))
            token?.let {token->
                updateTimeStore.getHonors.first().let { updateTime->
                    val result = admittedRetrofitRemote.getHonors(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {
                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.honors
                            response.updateTime?.let {
                                //儲存更新時間
                                updateTimeStore.saveHonors(it)
                            }
                            response.honors?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { data -> data.actionType == 2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                honorsDao.deleteHonors(deleteList as ArrayList<Honors>)
                                honorsDao.insertHonorsList(existList as ArrayList<Honors>)
                                //取出資料庫內的資料
                                result.data?.data =honorsDao.getAll()
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postTest(test: Test): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_TESTS))
            token?.let {
                val result = admittedRetrofitRemote.postTest(it,test)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java)
                        test.testId =response.id
                        testDao.insertTest(test)
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun postTestLists(tests: ArrayList<Test>): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_TESTS))
            token?.let {
                val result = admittedRetrofitRemote.postTestLists(it,tests)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
//                        var response =  Gson().fromJson(
//                            AdmittedEncrption().deCryption(message),
//                            TestListResponse::class.java)
                        testDao.insertTestList(tests)
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putTest(test: Test): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_TESTS))
            token?.let {
                val result = admittedRetrofitRemote.putTest(it,test)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    testDao.insertTest(test)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteTest(test: Test): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_TESTS))
            token?.let {
                val result = admittedRetrofitRemote.deleteTest(it,test)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    testDao.deleteTest(test)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFlowTestCached(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getTestCached()?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }
    private fun getTestCached(): NetworkResult<CommonResponse>? = testDao.getAll()?.let {
        NetworkResult.success(Action.CACHE_TESTS,CommonResponse(data = it), 0)
    }

    suspend fun getTest(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getTestCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_TESTS))
            token?.let {token->
                updateTimeStore.getTest.first().let { updateTime->
                    val result = admittedRetrofitRemote.getTest(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {

                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.tests
                            response.updateTime?.let {
                                //儲存更新時間
                                updateTimeStore.saveTest(it)
                            }
                            response.tests?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                testDao.deleteTest(deleteList as ArrayList<Test>)
                                testDao.insertTestList(existList as ArrayList<Test>)
                                //取出資料庫內的資料
                                result.data?.data =testDao.getAll()
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postCourse(course: Course): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_COURSE))
            token?.let {
                val result = admittedRetrofitRemote.postCourse(it,course)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java)
                        course.academicId =response.id
                        courseDao.insertCourse(course)
                    }

                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putCourse(course: Course): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_COURSE))
            token?.let {
                val result = admittedRetrofitRemote.putCourse(it,course)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    courseDao.insertCourse(course)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteCourse(course: Course): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_COURSE))
            token?.let {
                val result = admittedRetrofitRemote.deleteCourse(it,course)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    courseDao.deleteCourse(course)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFlowCourseCached(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getCourseCached()?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }
    private fun getCourseCached(): NetworkResult<CommonResponse>? = courseDao.getAll()?.let {
        NetworkResult.success(Action.CACHE_COURSE,CommonResponse(data = it), 0)
    }

    suspend fun getAll(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.GET_ALL))
            token?.let {token->

                updateTimeStore.getGetAll.first().let { updateTime ->
                    val result = admittedRetrofitRemote.getAll(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {
                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.courses
                            response.updateTime?.let {
                                updateTimeStore.saveGetAll(it)
                            }
                            response.user?.let {
                                keyValueDao.saveUserInfo(it)
                            }
                            response.intellectualCuriosies?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { intellectualCuriosity -> intellectualCuriosity.actionType!!.equals("2") }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { intellectualCuriosity -> !intellectualCuriosity.actionType!!.equals("2") }.collect(Collectors.toList())

                                intellectualCuriosityDao.deleteIntellectualCuriosities(deleteList as ArrayList<IntellectualCuriosity>)
                                intellectualCuriosityDao.insertIntellectualCuriosityList(existList as ArrayList<IntellectualCuriosity>)
                            }
                            response.communityImpacts?.let {
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                communityImpactDao.deleteCommunityImpacts(deleteList as ArrayList<CommunityImpact>)
                                communityImpactDao.insertCommunityImpactList(existList as ArrayList<CommunityImpact>)
                            }
                            response.honors?.let {
                                var deleteList = it.stream().filter { data -> data.actionType == 2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                honorsDao.deleteHonors(deleteList as ArrayList<Honors>)
                                honorsDao.insertHonorsList(existList as ArrayList<Honors>)
                            }
                            response.tests?.let {
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                testDao.deleteTest(deleteList as ArrayList<Test>)
                                testDao.insertTestList(existList as ArrayList<Test>)
                            }
                            response.courses?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                courseDao.deleteCourses(deleteList as ArrayList<Course>)
                                courseDao.insertCourseList(existList as ArrayList<Course>)
                            }
                            response.academicYears?.let {
                                keyValueDao.saveAcademicYears(response)
                            }
                            response.targetSchools?.let {
                                if (it.size>0){
                                    var lists = ArrayList<UserTargetSchool>()
                                    it.forEach {
                                        var userTargetSchool = UserTargetSchool(it.schoolId)
                                        lists.add(UserTargetSchool(it.schoolId))
                                    }
                                    keyValueDao.saveUserTargetschools(lists)
                                }

                            }
                            response.applicationSchools?.let {
                                var applicationSchools = ArrayList<ApplicationSchool>()
                                for (item in it){
                                    applicationSchools.add(item)
                                }
                                keyValueDao.saveApplicationSchools(applicationSchools)
                            }

                        }
                        emit(result)
                    }else{
                        emit(result)
                    }

                }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCourse(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getCourseCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_COURSE))
            token?.let {token->
                updateTimeStore.getCourse.first().let { updateTime ->
                    val result = admittedRetrofitRemote.getCourse(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {
                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.courses
                            response.updateTime?.let {
                                updateTimeStore.saveCourse(it)
                            }
                            response.courses?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                courseDao.deleteCourses(deleteList as ArrayList<Course>)
                                courseDao.insertCourseList(existList as ArrayList<Course>)
                                //取出資料庫內的資料
                                result.data?.data =courseDao.getAll()
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getAcademicYear(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
//            getAcadeicYearCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_ACADEMICYEAR))
            token?.let {
                val result = admittedRetrofitRemote.getAcademicYear(it)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response = deCryption(message)
                        result.data.data =response.academicYears
                        keyValueDao.saveAcademicYears(response)
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putAcademicYear(academicYear: AcademicYear): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.PUT_ACADEMICYEAR))
            token?.let {
                val result = admittedRetrofitRemote.putAcademicYear(it,academicYear)

                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveAcademicYear(academicYear)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteAcademicYear(academicYear: AcademicYear): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_ACADEMICYEAR))
            token?.let {
                val result = admittedRetrofitRemote.deleteAcademicYear(it,academicYear)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    academicYear.grade = "0"
                    keyValueDao.saveAcademicYear(academicYear)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * @param isStudent 用來給家長取得所選學生的個人資訊用
     */
    suspend fun putUserBasic(userInfo: UserInfo, isStudent: Boolean = false): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.PUT_USERBASIC))

            val token = if (isStudent) {
                keyValueDao.getStudentToken()
            } else {
                keyValueDao.getToken()
            }

            token?.let {
                val result = admittedRetrofitRemote.putUserBasic(it,userInfo.toJsonEncode())
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.let {
                        it.data = userInfo
                    }
                }
                emit(result)
//                if (result.status == NetworkResult.Status.SUCCESS) {
//                    userStore.saveUserBasic(userInfo)
//                }

            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * @param isStudent 用來給家長取得所選學生的個人資訊用
     */
    suspend fun getFlowUserBasic(isStudent: Boolean = false): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getUserBasicCache(isStudent)?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }

    /**
     * @param isStudent 用來給家長取得所選學生的個人資訊用
     */
    private fun getUserBasicCache(isStudent: Boolean = false): NetworkResult<CommonResponse>? {
        val userInfo = if (isStudent) keyValueDao.getStudentUserInfo()
                        else keyValueDao.getUserInfo()
        return userInfo?.let {
            NetworkResult.success(Action.CACHE_USERBASIC,CommonResponse(data = it), 0)
        }
    }

    /**
     * @param isStudent 用來給家長取得所選學生的個人資訊用
     */
    suspend fun cacheUserBasic(isStudent: Boolean = false): UserInfo? {
        return if (isStudent) keyValueDao.getStudentUserInfo()
                else keyValueDao.getUserInfo()
    }

    /**
     * @param isStudent 用來給家長取得所選學生的個人資訊用
     */
    suspend fun getUserBasic(isStudent: Boolean = false): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.GET_USERBASIC))

            val token = if (isStudent) {
                keyValueDao.getStudentToken()
            } else {
                keyValueDao.getToken()
            }

            token?.let { it ->
                val result = admittedRetrofitRemote.getUserBasic(it)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.let {
                        it.message?.let { message ->
                            val response = deCryption(message)
                            result.data.data = response.user
                            if (isStudent) {
                                keyValueDao.saveStudentUserInfo(response.user)
                            } else {
                                keyValueDao.saveUserInfo(response.user)

                                // 儲存學生列表 (For 家長)
                                response.user.students?.let { students ->
                                    keyValueDao.saveStudents(students)

                                    // 儲存選擇的學生 (For 家長)
                                    response.user.student?.systemNumber?.let { studentId ->
                                        students.firstOrNull { student ->
                                            student.systemNumber == studentId
                                        }?.let { student ->
                                            // FIXME:
                                            keyValueDao.saveStudentTokenAndId(
                                                student.token,
                                                student.systemNumber,
                                                student.firstName ?: ""
                                            )
                                        }
                                    }
                                }
                            }
//                            userStore.saveUserBasic(response.user)
                        }
                    }
                }
                emit(result)
            }
//            keyValueDao.getToken()?.let {
//
//                val result = admittedRetrofitRemote.getUserBasic(it)
//                if (result.status == NetworkResult.Status.SUCCESS) {
//                    result.data?.let {
//                        it.message?.let {
//                            result.data.data = deCryption(it).user
//                            userStore.saveUserBasic(deCryption(it).user)
//                        }
//                    }
//                }
//                emit(result)
//            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun login(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.LOGIN))
            val result = admittedRetrofitRemote.login(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //登入成功
                result.data?.message?.let { message ->
                    //解碼成功字串
                    val response = deCryption(message)
                    keyValueDao.saveToken(response.token)
                    Log.d("login", "token: ${response.token}")

                    response.data?.type?.let { userType ->
                        keyValueDao.saveUserType(userType)
                    }

                    result.data.data = response
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun postDeviceToken(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DEVICE_TOKEN))
            token?.let {
                val result = admittedRetrofitRemote.postDeviceToken(it, call)
                emit(result)
            }

        }.flowOn(Dispatchers.IO)
    }

    suspend fun postDeviceToken(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DEVICE_TOKEN))
            token?.let {token->
                keyValueDao.getDeviceToken()?.let {deviceToken->
                    val result = admittedRetrofitRemote.postDeviceToken(token, DeviceTokenRequest(deviceToken).toJsonEncode())
                    emit(result)
                }
            }

        }.flowOn(Dispatchers.IO)
    }
    suspend fun loginGoogle(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.LOGIN_GOOGLE))
            val result = admittedRetrofitRemote.loginGoogle(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //登入成功
                result.data?.message?.let { message ->
                    //解碼成功字串
                    var response =  deCryption(message)
                    keyValueDao.saveToken(response.token)
                    result.data.data = response
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loginApplee(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.LOGIN_APPLE))
            val result = admittedRetrofitRemote.loginApple(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //登入成功
                result.data?.message?.let { message ->
                    //解碼成功字串
                    var response =  deCryption(message)
                    keyValueDao.saveToken(response.token)
                    result.data.data = response
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun forgotPasword(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.FORGOTPASSWORD))
            val result = admittedRetrofitRemote.forgotPasword(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //修改密碼成功
                result.data?.message?.let { message ->
                    result.data.data = deCryption(message)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun editPasword(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.EDITPASSWORD))
            val result = admittedRetrofitRemote.editPasword(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //忘記密碼成功
                result.data?.message?.let { message ->
                    result.data.data = deCryption(message)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun register(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.REGISTER))
            val result = admittedRetrofitRemote.register(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //忘記密碼成功
                result.data?.message?.let { message ->
                    result.data.data = deCryption(message)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerGetCode(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.REGISTERGETCODE))
            val result = admittedRetrofitRemote.registerGetCode(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //忘記密碼成功
                result.data?.message?.let { message ->
                    result.data.data = deCryption(message)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerCheck(call: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.REGISTERCHECK))
            val result = admittedRetrofitRemote.registerCheck(call)
            if (result.status == NetworkResult.Status.SUCCESS) {
                //成功
                result.data?.message?.let { message ->
                    result.data.data = deCryption(message)
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_INTELLECTUALCURIOSITY))
            token?.let {
                val result = admittedRetrofitRemote.postIntellectualCuriosity(it,intellectualCuriosity)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java)
                        intellectualCuriosity.id =response.id
                        intellectualCuriosityDao.insertIntellectualCuriosity(intellectualCuriosity)
                    }

                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_INTELLECTUALCURIOSITY))
            token?.let {
                val result = admittedRetrofitRemote.putIntellectualCuriosity(it,intellectualCuriosity)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    intellectualCuriosityDao.insertIntellectualCuriosity(intellectualCuriosity)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_INTELLECTUALCURIOSITY))
            token?.let {
                val result = admittedRetrofitRemote.deleteIntellectualCuriosity(it,intellectualCuriosity)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    intellectualCuriosityDao.deleteIntellectualCuriosity(intellectualCuriosity)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFlowIntellectualCuriosityCached(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getIntellectualCuriosityCached()?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }
    private fun getIntellectualCuriosityCached(): NetworkResult<CommonResponse>? = intellectualCuriosityDao.getAll()?.let {
        NetworkResult.success(Action.CACHE_INTELLECTUALCURIOSITY,CommonResponse(data = it), 0)
    }

    suspend fun getIntellectualCuriosity(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getIntellectualCuriosityCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_INTELLECTUALCURIOSITY))
            token?.let {token->
                updateTimeStore.getIntellectualcuriosity.first().let { updateTime ->
                    var result = admittedRetrofitRemote.getIntellectualCuriosity(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {

                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data?.data =response.intellectualCuriosies
                            response.updateTime?.let {
                                //儲存更新時間
                                updateTimeStore.saveIntellectualcuriosity(it)
                            }

                            response.intellectualCuriosies?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { intellectualCuriosity -> intellectualCuriosity.actionType!!.equals("2") }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { intellectualCuriosity -> !intellectualCuriosity.actionType!!.equals("2") }.collect(Collectors.toList())

                                intellectualCuriosityDao.deleteIntellectualCuriosities(deleteList as ArrayList<IntellectualCuriosity>)
                                intellectualCuriosityDao.insertIntellectualCuriosityList(existList as ArrayList<IntellectualCuriosity>)
                                //取出資料庫內的資料
                                result.data?.data =intellectualCuriosityDao.getAll()
                            }

                        }
                    }
                    emit(result)
                }

            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postCommunityImpact(communityImpact: CommunityImpact): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_COMMUNITYIMPACT))
            token?.let {
                val result = admittedRetrofitRemote.postCommunityImpact(it,communityImpact)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java)
                        communityImpact.id =response.id
                        communityImpactDao.insertCommunityImpact(communityImpact)
                    }

                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putCommunityImpact(communityImpact: CommunityImpact): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.PUT_COMMUNITYIMPACT))
            token?.let {
                val result = admittedRetrofitRemote.putCommunityImpact(it,communityImpact)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    communityImpactDao.insertCommunityImpact(communityImpact)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteCommunityImpact(communityImpact: CommunityImpact): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_COMMUNITYIMPACT))
            token?.let {
                val result = admittedRetrofitRemote.deleteCommunityImpact(it,communityImpact)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    communityImpactDao.deleteCommunityImpact(communityImpact)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFlowCommunityImpactCached(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getCommunityImpactCached()?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }
    private fun getCommunityImpactCached(): NetworkResult<CommonResponse>? = communityImpactDao.getAll()?.let {
        NetworkResult.success(Action.CACHE_COMMUNITYIMPACT,CommonResponse(data = it), 0)
    }
    private fun getNotificationCached(): NetworkResult<CommonResponse>? = fcMessageDao.getAll()?.let {
        NetworkResult.success(Action.CACHE_NOTIFICATION,CommonResponse(data = it), 0)
    }

    suspend fun getFcmMessage(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getNotificationCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_NOTIFICATION))
            token?.let {token->
                updateTimeStore.getNotification.first().let { updateTime ->

                    val result = admittedRetrofitRemote.getNotification(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {

                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.notifications
                            response.updateTime?.let {
                                updateTimeStore.saveNotification(it)
                            }
                            response.notifications?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                fcMessageDao.deleteFCMessage(deleteList as ArrayList<FCMessage>)
                                fcMessageDao.insertFCMessageList(existList as ArrayList<FCMessage>)
                                //取出資料庫內的資料
                                result.data?.data =fcMessageDao.getAll()
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getCommunityImpact(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getCommunityImpactCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_COMMUNITYIMPACT))
            token?.let {token->
                updateTimeStore.getCommunityimpact.first().let { updateTime ->

                    val result = admittedRetrofitRemote.getCommunityImpact(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {

                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.communityImpacts
                            response.updateTime?.let {
                                updateTimeStore.saveCommunityimpact(it)
                            }
                            response.communityImpacts?.let {
                                //取出要刪除的list
                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                var existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                communityImpactDao.deleteCommunityImpacts(deleteList as ArrayList<CommunityImpact>)
                                communityImpactDao.insertCommunityImpactList(existList as ArrayList<CommunityImpact>)
                                //取出資料庫內的資料
                                result.data?.data =communityImpactDao.getAll()
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTargetSchoolDetailList(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
//            getTargetSchoolCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GETTARGETSCHHOOL_DETAIL_LIST))
            keyValueDao.getToken()?.let { token ->
                updateTimeStore.getTargetSchool.first().let { updateTime ->
                    val result = admittedRetrofitRemote.getTargetSchoolDetailList(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {

                        result.data?.message?.let { message ->
                            val response = deCryption(message)
                            result.data.data = response.targetSchools

                            response.targetSchools?.let {
                                //取出要刪除的list
                                val deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
                                val existList = it.stream().filter { data -> data.actionType!=2 }.collect(Collectors.toList())

                                targetSchoolDao.deleteTargetSchools(deleteList as ArrayList<TargetSchool>)
                                targetSchoolDao.insertTargetSchoolList(existList as ArrayList<TargetSchool>)
                                //取出資料庫內的資料
                                result.data?.data = targetSchoolDao
                                    .getAll()
                                    ?.filter { it1 -> it1.isEnabled }
                            }
                            response.updateTime?.let {
                                updateTimeStore.saveTargetSchhol(it)
                            }
                        }
                    }
                    emit(result)
                }
//                updateTimeStore.getTargetSchool.collect() {
//                }
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getTargetSchoolBriefList(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
//            getTargetSchoolCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GETTARGETSCHHOOL_BRIEFT))
            token?.let {token->
                updateTimeStore.getTargetSchool.first().let { updateTime ->

                    val result = admittedRetrofitRemote.getTargetSchoolBriefList(token,updateTime)
                    if (result.status == NetworkResult.Status.SUCCESS) {
                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data?.data = response.targetSchools
                        }

                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTargetSchoolDetail(id:String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
//            getTargetSchoolCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GETTARGETSCHHOOL_DETAIL_ID))
            token?.let {token->
                updateTimeStore.getTargetSchool.first().let { updateTime ->

                    val result = admittedRetrofitRemote.getTargetSchoolDetail(token,id)
                    if (result.status == NetworkResult.Status.SUCCESS) {
                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data?.data = response.targetSchool
                        }

                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getFlowTargetSchoolCached(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            getTargetSchoolCached()?.let { emit(it) }

        }.flowOn(Dispatchers.IO)
    }
    private fun getTargetSchoolCached(): NetworkResult<CommonResponse>? = targetSchoolDao.getAll()?.let {
        NetworkResult.success(Action.CACHETARGETSCHHOOL_DETAIL_LIST,CommonResponse(data = it), 0)
    }


    suspend fun getUserTargetSchool(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
//            getTargetSchoolCached()?.let { emit(it) }
            emit(NetworkResult.loading(action = Action.GET_USERTARGETSCHHOOL))
            token?.let {token->
                    val result = admittedRetrofitRemote.getUserTargetSchool(token)
                    if (result.status == NetworkResult.Status.SUCCESS) {

                        result.data?.message?.let { message ->
                            var response = deCryption(message)
                            result.data.data = response.targetSchools

//                            response.updateTime?.let {
//                                updateTimeStore.saveTargetSchhol(it)
//                            }
                            response.targetSchools?.let {
                                var userTargetSchools = ArrayList<UserTargetSchool>()
                                for (item in it){
                                    userTargetSchools.add(UserTargetSchool(item.schoolId))
                                }
                                keyValueDao.saveUserTargetschools(userTargetSchools)

                                //取出要刪除的list
//                                var deleteList = it.stream().filter { data -> data.actionType==2 }.collect(Collectors.toList())
                                //取出要更新的List
//                                var existList = it.stream().filter { data -> !data -> data.actionType==2 }.collect(Collectors.toList())

//                                targetSchoolDao.deleteTargetSchools(deleteList as ArrayList<TargetSchool>)
//                                targetSchoolDao.insertTargetSchoolList(existList as ArrayList<TargetSchool>)
                                //取出資料庫內的資料
                                result.data?.data =keyValueDao.getUserTargetschools()
                            }
                        }

                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun postUserTargetSchool(userTargetSchool: UserTargetSchool): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.postUserTargetSchool(it,userTargetSchool)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveUserTargetschool(userTargetSchool)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postUserTargetSchoolList(userTargetSchools: ArrayList<UserTargetSchool>): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.postUserTargetSchoolList(it,userTargetSchools)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveUserTargetschools(userTargetSchools)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteUserTargetSchool(userTargetSchool: UserTargetSchool): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.deleteUserTargetSchool(it,userTargetSchool)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.deleteUserTargetSchool(userTargetSchool)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun putUserTargetSchool(userTargetSchools: ArrayList<UserTargetSchool>): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.PUT_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.putUserTargetSchool(it,userTargetSchools)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveUserTargetschools(userTargetSchools)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getApplicationSchool(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.GET_APPLICATIONSCHHOOL))
            token?.let {token->
                val result = admittedRetrofitRemote.getApplicationSchool(token)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        val response = deCryption(message)
                        result.data.data = response.applicationSchools
                        response.applicationSchools.let {
                            val applicationSchools = ArrayList<ApplicationSchool>()
                            for (item in it){
                                applicationSchools.add(item)
                            }
                            keyValueDao.saveApplicationSchools(applicationSchools)
                            result.data.data =keyValueDao.getApplicationSchools()
                        }
                    }
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getApplicationSchoolForCheckSubscribel(): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.GET_APPLICATIONSCHHOOL))
            token?.let { token->
                val result = admittedRetrofitRemote.getApplicationSchool(token)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    emit(result)
                }else{
                    emit(result)
                }
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun postApplicationSchool(applicationSchool: ApplicationSchool): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.postApplicationSchool(it,applicationSchool)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveApplicationSchool(applicationSchool)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun putApplicationSchool(applicationSchool: ApplicationSchool): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.putApplicationSchool(it,applicationSchool)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveApplicationSchool(applicationSchool)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteApplicationSchool(applicationSchool: ApplicationSchool): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.DELETE_USERTARGETSCHHOOL))
            token?.let {
                val result = admittedRetrofitRemote.deleteApplicationSchool(it,applicationSchool)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.deleteApplicationSchool(applicationSchool)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postSubscribe(subscribe: Subscribe): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>> {
            emit(NetworkResult.loading(action = Action.POST_SUBSCRIBE))
            keyValueDao.getToken()?.let {
                val result = admittedRetrofitRemote.postSubscribe(it,subscribe)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        var response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java)
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /** 設定用戶類型 */
    fun putUserType(userType: UserType): Flow<NetworkResult<CommonResponse>?> {
        return flow {
            emit(NetworkResult.loading(action = Action.PUT_USER_TYPE))
            keyValueDao.getToken()?.let {
                val result = admittedRetrofitRemote.putUserType(it, userType)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    keyValueDao.saveUserType(userType.intValue)
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /** 該家長所有的學生資訊 */
    fun getStudents(): Flow<NetworkResult<CommonResponse>?> {
        return flow {
            emit(NetworkResult.loading(action = Action.GET_STUDENTS))
            keyValueDao.getToken()?.let {
                val result = admittedRetrofitRemote.getStudentsUnderParent(it)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        val response =  Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java
                        )
                        keyValueDao.saveStudents(response.students)

                        result.data.data = response
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /** 搜尋學生
     *
     * @param id 學生systemNumber
     */
    fun searchStudent(id: String): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>?> {
            emit(NetworkResult.loading(action = Action.SEARCH_STUDENT))
            keyValueDao.getToken()?.let {
                val result = admittedRetrofitRemote.searchStudent(it, id)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        val response = Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java
                        )
                        result.data.data = response
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /** 新增學生到家長/ 老師底下
     *
     * @param id 學生systemNumber
     * @param request 訂閱資訊
     */
    fun addStudentToParent(
        id: String,
        subscribe: Subscribe
    ): Flow<NetworkResult<CommonResponse>?> {
        return flow<NetworkResult<CommonResponse>?> {
            emit(NetworkResult.loading(action = Action.ADD_STUDENT_TO_PARENT))
            keyValueDao.getToken()?.let {
                val request = PostUserStudentRequest(
                    method = subscribe.method,
                    type = subscribe.type ?: "",
                    subscriptionAt = subscribe.subscriptionAt,
                    amount = subscribe.amount,
                    currency = subscribe.currency,
                    payInfo = subscribe.payInfo ?: ""
                )

                val result = admittedRetrofitRemote.addStudentToParent(it, id, request)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        val response = Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java
                        )
                        result.data.data = response
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /** 刪除該家長的學生
     *
     * @param id 學生systemNumber
     */
    fun deleteStudentUnderParent(
        id: String,
        syncLocal: Boolean = true
    ): Flow<NetworkResult<CommonResponse>?> {
        return flow {
            emit(NetworkResult.loading(action = Action.DELETE_STUDENT_FROM_PARENT))
            keyValueDao.getToken()?.let {
                val result = admittedRetrofitRemote.deleteStudentUnderParent(it, id)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        val response = Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java
                        )

                        if (syncLocal) {
                            keyValueDao.deleteStudent(Student(systemNumber = id))
                        }

                        result.data.data = response
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    /** 設定/ 解除目前查看的學生
     *
     * @param id 學生systemNumber
     */
    fun selectStudent(
        id: String,
        isCurrent: Boolean,
        studentToken: String? = null,
        studentName: String? = null
    ): Flow<NetworkResult<CommonResponse>?> {
        return flow {
            emit(NetworkResult.loading(action = Action.PUT_USER_STUDENT))
            keyValueDao.getToken()?.let {
                val result = admittedRetrofitRemote.putUserStudent(it, id, isCurrent)
                if (result.status == NetworkResult.Status.SUCCESS) {
                    result.data?.message?.let { message ->
                        val response = Gson().fromJson(
                            AdmittedEncrption().deCryption(message),
                            DeCryptionResponse::class.java
                        )

                        if (isCurrent) {
                            studentToken?.let {
                                keyValueDao.saveStudentTokenAndId(
                                    studentToken,
                                    id,
                                    studentName ?: ""
                                )
                            }
                        } else {
                            keyValueDao.clearStudentTokenAndId()
                        }

                        result.data.data = response
                    }
                }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }
}
