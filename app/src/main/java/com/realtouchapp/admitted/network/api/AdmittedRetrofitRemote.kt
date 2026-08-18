package com.realtouchapp.admitted.network.api

import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.UserTargetSchoolRequest
import com.realtouchapp.admitted.network.request.user.AcademicYearRequest
import com.realtouchapp.admitted.network.request.user.PostUserStudentRequest
import com.realtouchapp.admitted.network.request.user.PutUserStudentRequest
import com.realtouchapp.admitted.network.request.user.TestListsRequest
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.request.user.UserTypeRequest
import com.realtouchapp.admitted.network.result.base.NetworkResult.Action
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.tools.ext.toJsonEncode
import okio.IOException
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
專門在處理遠端的資料溝通
 */
class AdmittedRetrofitRemote @Inject constructor(private val retrofit: Retrofit) {
    init {

    }

    /** 取得學校簡章簡易資訊 */
    suspend fun getTargetSchoolBriefList(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GETTARGETSCHHOOL_BRIEFT,
            request = { apiService.getTargetSchoolBriefList("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getTargetSchoolBriefList")
    }
    
    /** 取得學校簡章詳細資訊 */
    suspend fun getTargetSchoolDetailList(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GETTARGETSCHHOOL_DETAIL_LIST,
            request = { apiService.getTargetSchoolDetailList("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getTargetSchoolDetailList")
    }
    
    /** 取得學校簡章詳細資訊 */
    suspend fun getTargetSchoolDetail(userToken:String,id:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GETTARGETSCHHOOL_DETAIL_ID,
            request = { apiService.getTargetSchoolDetail("Bearer $userToken",id) },
            defaultErrorMessage = "Error getTargetSchoolDetail")
    }

    /** 取得用戶目標學校 */
    suspend fun getUserTargetSchool(userToken:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_USERTARGETSCHHOOL,
            request = { apiService.getUserTargetSchool("Bearer $userToken") },
            defaultErrorMessage = "Error getUserTargetSchool")
    }

    /** 編輯用戶目標學校 */
    suspend fun putUserTargetSchool(userToken:String,userTargetSchools: ArrayList<UserTargetSchool>): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.PUT_USERTARGETSCHHOOL,
            request = { apiService.putUserTargetSchool("Bearer $userToken",UserTargetSchoolRequest(userTargetSchools).toJsonEncode()) },
            defaultErrorMessage = "Error putUserTargetSchool")
    }

    /** 建立用戶目標學校 */
    suspend fun postUserTargetSchoolList(userToken:String,userTargetSchools: ArrayList<UserTargetSchool>): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.POST_USERTARGETSCHHOOL,
            request = { apiService.postUserTargetSchoolList("Bearer $userToken",UserTargetSchoolRequest(userTargetSchools).toJsonEncode()) },
            defaultErrorMessage = "Error postUserTargetSchool")
    }

    /** 新增用戶目標學校 */
    suspend fun postUserTargetSchool(userToken:String,userTargetSchool: UserTargetSchool): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.POST_USERTARGETSCHHOOL,
            request = { apiService.postUserTargetSchool("Bearer $userToken",userTargetSchool.schoolId.toString()) },
            defaultErrorMessage = "Error postUserTargetSchool")
    }

    /** 建立用戶目標學校 */
    suspend fun deleteUserTargetSchoolList(userToken:String,userTargetSchools: ArrayList<UserTargetSchool>): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.DELETE_USERTARGETSCHHOOL,
            request = { apiService.postDeleteUserTargetSchoolList("Bearer $userToken",UserTargetSchoolRequest(userTargetSchools).toJsonEncode()) },
            defaultErrorMessage = "Error deleteUserTargetSchool")
    }
    
    /** 刪除用戶目標學校 */
    suspend fun deleteUserTargetSchool(userToken:String,userTargetSchool: UserTargetSchool): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_USERTARGETSCHHOOL,
            request = { apiService.deleteUserTargetSchool("Bearer $userToken",userTargetSchool.schoolId.toString()) },
            defaultErrorMessage = "Error deleteUserTargetSchool")
    }

    /** 取得用戶申請學校 */
    suspend fun getApplicationSchoolDetail(userToken:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.GET_APPLICATIONSCHHOOL_DETAIL,
            request = { apiService.getApplication("Bearer $userToken") },
            defaultErrorMessage = "Error putApplicationSchool")
    }

    /** 取得用戶申請學校 */
    suspend fun getApplicationSchool(userToken:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.GET_APPLICATIONSCHHOOL,
            request = { apiService.getApplication("Bearer $userToken") },
            defaultErrorMessage = "Error putApplicationSchool")
    }

    /** 編輯用戶申請學校 */
    suspend fun putApplicationSchool(userToken:String,applicationSchool: ApplicationSchool): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.PUT_APPLICATIONSCHHOOL,
            request = { apiService.putApplication("Bearer $userToken",applicationSchool.schoolId.toString(),
                    applicationSchool.toJsonEncode()) },
            defaultErrorMessage = "Error putApplicationSchool")
    }

    /** 建立用戶申請學校 */
    suspend fun postApplicationSchool(userToken:String,applicationSchool: ApplicationSchool): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);

        return getResponse(Action.POST_APPLICATIONSCHHOOL,
            request = { apiService.postApplication("Bearer $userToken",applicationSchool.toJsonEncode()) },
            defaultErrorMessage = "Error postApplicationSchool")
    }

    /** 刪除用戶申請學校 */
    suspend fun deleteApplicationSchool(userToken:String,applicationSchool: ApplicationSchool): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_APPLICATIONSCHHOOL,
            request = { apiService.deleteApplication("Bearer $userToken",applicationSchool.schoolId.toString()) },
            defaultErrorMessage = "Error deleteApplicationSchool")
    }
    
    /** 建立社會影響 */
    suspend fun postCommunityImpact(userToken:String,communityImpact: CommunityImpact): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_COMMUNITYIMPACT,
            request = { apiService.postCommunityImpact("Bearer $userToken",communityImpact.toJsonEncode()) },
            defaultErrorMessage = "Error postCommunityImpact")
    }
    
    /** 編輯社會影響 */
    suspend fun putCommunityImpact(userToken:String,communityImpact: CommunityImpact): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_COMMUNITYIMPACT,
            request = { apiService.putCommunityImpact("Bearer $userToken",communityImpact.id.toString(),communityImpact.toJsonEncode()) },
            defaultErrorMessage = "Error putCommunityImpact")
    }

    /** 刪除社會影響 */
    suspend fun deleteCommunityImpact(userToken:String,communityImpact: CommunityImpact): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_COMMUNITYIMPACT,
            request = { apiService.deleteCommunityImpact("Bearer $userToken",communityImpact.id.toString()) },
            defaultErrorMessage = "Error deleteCommunityImpact")
    }

    /** 取得社會影響 */
    suspend fun getCommunityImpact(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_COMMUNITYIMPACT,
            request = { apiService.getCommunityImpact("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getCommunityImpact")
    }

    /** 建立求知欲 */
    suspend fun postIntellectualCuriosity(userToken:String,intellectualCuriosity: IntellectualCuriosity): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_INTELLECTUALCURIOSITY,
            request = { apiService.postIntellectuaCuriosity("Bearer $userToken",intellectualCuriosity.toJsonEncode()) },
            defaultErrorMessage = "Error postIntellectualCuriosity")
    }

    /** 編輯求知欲 */
    suspend fun putIntellectualCuriosity(userToken:String,intellectualCuriosity: IntellectualCuriosity): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_INTELLECTUALCURIOSITY,
            request = { apiService.putIntellectuaCuriosity("Bearer $userToken",intellectualCuriosity.id.toString(),intellectualCuriosity.toJsonEncode()) },
            defaultErrorMessage = "Error putIntellectualCuriosity")
    }

    /** 刪除求知欲 */
    suspend fun deleteIntellectualCuriosity(userToken:String,intellectualCuriosity: IntellectualCuriosity): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_INTELLECTUALCURIOSITY,
            request = { apiService.deleteIntellectuaCuriosity("Bearer $userToken",intellectualCuriosity.id.toString()) },
            defaultErrorMessage = "Error deleteIntellectualCuriosity")
    }

    /** 取得求知欲 */
    suspend fun getIntellectualCuriosity(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_INTELLECTUALCURIOSITY,
            request = { apiService.getIntellectuaCuriosity("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getIntellectualCuriosity")
    }

    /** 建立在校成績 */
    suspend fun postCourse(userToken:String,course: Course): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_COURSE,
            request = { apiService.postAcademic("Bearer $userToken",course.toJsonEncode()) },
            defaultErrorMessage = "Error postCourse")
    }

    /** 編輯基本資料 */
    suspend fun putCourse(userToken:String,course: Course): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_COURSE,
            request = { apiService.putAcademic("Bearer $userToken",course.academicId.toString(),course.toJsonEncode()) },
            defaultErrorMessage = "Error putCourse")
    }

    /** 編輯基本資料 */
    suspend fun deleteCourse(userToken:String,course: Course): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_COURSE,
            request = { apiService.deleteAcademic("Bearer $userToken",course.academicId.toString()) },
            defaultErrorMessage = "Error deleteCourse")
    }
    
    /** 取得當前用戶所有資料 */
    suspend fun getAll(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_ALL,
            request = { apiService.getAll("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getAll")
    }
    
    /** 取得推播紀錄 */
    suspend fun getNotification(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(
            Action.GET_NOTIFICATION,
            request = { apiService.getNotifications("Bearer $userToken", updateTime) },
            defaultErrorMessage = "Error getCourse"
        )
    }

    /** 取得在校成績 */
    suspend fun getCourse(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_COURSE,
            request = { apiService.getAcademic("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getCourse")
    }

    /** 建立測驗成績 */
    suspend fun postTest(userToken:String,test: Test): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_TESTS,
            request = { apiService.postTest("Bearer $userToken",test.toJsonEncode()) },
            defaultErrorMessage = "Error postTest")
    }
    
    /** 編輯測驗成績 */
    suspend fun putTest(userToken:String,test: Test): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_TESTS,
            request = { apiService.putTest("Bearer $userToken",test.testId.toString(),test.toJsonEncode()) },
            defaultErrorMessage = "Error putTest")
    }

    /** 建立測驗成績 */
    suspend fun postTestLists(userToken:String,tests: ArrayList<Test>): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_TESTLISTS,
            request = { apiService.postTestLists("Bearer $userToken",TestListsRequest(tests).toJsonEncode()) },
            defaultErrorMessage = "Error putTest")
    }

    /** 刪除測驗成績 */
    suspend fun deleteTest(userToken:String,test: Test): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_TESTS,
            request = { apiService.deleteTest("Bearer $userToken",test.testId.toString()) },
            defaultErrorMessage = "Error deleteTest")
    }

    /** 取得測驗成績 */
    suspend fun getTest(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_TESTS,
            request = { apiService.getTest("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getTest")
    }

    /** 建立榮譽事蹟 */
    suspend fun postHonors(userToken:String,honors: Honors): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_HONORS,
            request = { apiService.postHonors("Bearer $userToken",honors.toJsonEncode()) },
            defaultErrorMessage = "Error postHonors")
    }
    
    /** 編輯榮譽事蹟 */
    suspend fun putHonors(userToken:String,honors: Honors): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_HONORS,
            request = { apiService.putHonors("Bearer $userToken",honors.id.toString(),honors.toJsonEncode()) },
            defaultErrorMessage = "Error putHonors")
    }

    /** 刪除榮譽事蹟 */
    suspend fun deleteHonors(userToken:String,honors: Honors): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_HONORS,
            request = { apiService.deleteHonors("Bearer $userToken",honors.id.toString()) },
            defaultErrorMessage = "Error deleteHonors")
    }

    /** 取得榮譽事蹟 */
    suspend fun getHonors(userToken:String,updateTime:String?): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_HONORS,
            request = { apiService.getHonors("Bearer $userToken",updateTime) },
            defaultErrorMessage = "Error getHonors")
    }

    /** 刪除在校學年總成績 */
    suspend fun putAcademicYear(userToken:String,academicYear: AcademicYear): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_ACADEMICYEAR,
            request = { apiService.putAacademicyear("Bearer $userToken",academicYear.academicYearId,AcademicYearRequest(academicYear.grade).toJsonEncode()) },
            defaultErrorMessage = "Error getAcademicYear")
    }

    /** 刪除在校學年總成績 */
    suspend fun deleteAcademicYear(userToken:String,academicYear: AcademicYear): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DELETE_ACADEMICYEAR,
            request = { apiService.deleteAcademicyear("Bearer $userToken",academicYear.academicYearId.toString()) },
            defaultErrorMessage = "Error getAcademicYear")
    }

    /** 取得在校學年總成績 */
    suspend fun getAcademicYear(userToken:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_ACADEMICYEAR,
            request = { apiService.getAcademicyear("Bearer $userToken") },
            defaultErrorMessage = "Error getAcademicYear")
    }

    /** 登入 */
    suspend fun login(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.LOGIN,
            request = { apiService.login(call) },
            defaultErrorMessage = "")
    }

    /** 推播裝置存儲token */
    suspend fun postDeviceToken(token:String,call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.DEVICE_TOKEN,
            request = { apiService.deviceToken("Bearer $token",call) },
            defaultErrorMessage = "")
    }

    /** Google登入 */
    suspend fun loginGoogle(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.LOGIN_GOOGLE,
            request = { apiService.loginGoogle(call) },
            defaultErrorMessage = "")
    }

    /** Apple登入 */
    suspend fun loginApple(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.LOGIN_APPLE,
            request = { apiService.loginApple(call) },
            defaultErrorMessage = "")
    }

    /** 取得基本資料 */
    suspend fun getUserBasic(userToken:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.GET_USERBASIC,
            request = { apiService.getUserBasic("Bearer $userToken") },
            defaultErrorMessage = "")
    }

    /** 編輯基本資料 */
    suspend fun putUserBasic(userToken:String,call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.PUT_USERBASIC,
            request = { apiService.putUserBasic("Bearer $userToken",call) },
            defaultErrorMessage = "")
    }

    /** 忘記密碼 */
    suspend fun forgotPasword(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.FORGOTPASSWORD,
            request = { apiService.forgotPasword(call) },
            defaultErrorMessage = "")
    }

    /** 修改密碼 */
    suspend fun editPasword(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.EDITPASSWORD,
            request = { apiService.editPasword(call) },
            defaultErrorMessage = "")
    }

    /** 註冊 */
    suspend fun register(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.REGISTER,
            request = { apiService.register(call) },
            defaultErrorMessage = "")
    }

    /** 檢查帳號是否能注冊 */
    suspend fun registerCheck(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.REGISTERCHECK,
            request = { apiService.registerCheck(call) },
            defaultErrorMessage = "")
    }

    /** 獲取驗證碼 */
    suspend fun registerGetCode(call:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java);
        return getResponse(Action.REGISTERGETCODE,
            request = { apiService.registerGetCode(call) },
            defaultErrorMessage = "")
    }

    /** 建立測驗成績 */
    suspend fun postSubscribe(userToken:String,subscribe: Subscribe): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.POST_SUBSCRIBE,
            request = { apiService.postSubscribe("Bearer $userToken",subscribe.toJsonEncode()) },
            defaultErrorMessage = "Error POST_SUBSCRIBE")
    }

    /** 設定用戶類型 */
    suspend fun putUserType(userToken:String, userType: UserType): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_USER_TYPE,
            request = {
                apiService.putUserType(
                    "Bearer $userToken",
                    UserTypeRequest(userType.intValue).toJsonEncode()
                )
            },
            defaultErrorMessage = "Error PUT_USER_TYPE")
    }

    /** 該家長所有的學生資訊 */
    suspend fun getStudentsUnderParent(userToken:String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.GET_STUDENTS,
            request = {
                apiService.getStudentsUnderParent(
                    "Bearer $userToken"
                )
            },
            defaultErrorMessage = "Error GET_STUDENTS")
    }

    /** 查詢學生並回傳學生姓名、Logo、系統編號、以及可使用的訂閱資訊。
     *
     * 已經訂閱回傳錯誤。
     *
     * @param userToken Token
     * @param id 學生systemNumber
     */
    suspend fun searchStudent(userToken: String, id: String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.SEARCH_STUDENT,
            request = {
                apiService.searchStudent(
                    "Bearer $userToken",
                    id,
                    "2"
                )
            },
            defaultErrorMessage = "Error SEARCH_STUDENT")
    }

    /** 新增學生到家長/ 老師底下
     *
     * @param userToken Token
     * @param id 學生systemNumber
     * @param request 訂閱資訊
     */
    suspend fun addStudentToParent(
        userToken: String,
        id: String,
        request: PostUserStudentRequest
    ): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.ADD_STUDENT_TO_PARENT,
            request = {
                apiService.addStudent(
                    "Bearer $userToken",
                    id,
                    request.toJsonEncode()
                )
            },
            defaultErrorMessage = "Error ADD_STUDENT_TO_PARENT")
    }

    /** 設定/ 解除目前查看的學生
     *
     * @param userToken Token
     * @param id 學生systemNumber
     */
    suspend fun putUserStudent(userToken: String, id: String, isCurrent: Boolean): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.PUT_USER_STUDENT,
            request = {
                apiService.putStudent(
                    "Bearer $userToken",
                    id,
                    PutUserStudentRequest(isCurrent).toJsonEncode()
                )
            },
            defaultErrorMessage = "Error PUT_USER_STUDENT")
    }

    /** 刪除該家長的學生
     *
     * @param userToken Token
     * @param id 學生systemNumber
     */
    suspend fun deleteStudentUnderParent(userToken: String, id: String): NetworkResult<CommonResponse> {
        val apiService = retrofit.create(AdmittedApi::class.java)
        return getResponse(Action.DELETE_STUDENT_FROM_PARENT,
            request = {
                apiService.deleteStudent(
                    "Bearer $userToken",
                    id
                )
            },
            defaultErrorMessage = "Error DELETE_STUDENT_FROM_PARENT")
    }

    private suspend fun <T> getResponse(action: Action, request: suspend () -> Response<T>, defaultErrorMessage: String): NetworkResult<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return  NetworkResult.success(action,result.body(),result.code())
            } else {
                val errorResponse = ErrorUtils.parseError(
                    result,
                    retrofit
                )
                NetworkResult.error(action,errorResponse?.message ?: defaultErrorMessage, errorResponse,statusCode = result.code())
            }
        } catch (e: Throwable) {
            NetworkResult.error(action,e.message!!, CommonResponse("error",e.message!!),statusCode = AdmittedApi.StatusCode.ERROR_OTTHER.code)
        }
    }
}

object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): CommonResponse? {
        val converter = retrofit.responseBodyConverter<CommonResponse>(CommonResponse::class.java, arrayOfNulls(0))

        try {
            return  converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            return CommonResponse(message =e.message)
        }
    }
}
