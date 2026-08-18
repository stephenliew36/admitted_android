package com.realtouchapp.admitted.network.api

import com.realtouchapp.admitted.network.result.base.CommonResponse
import retrofit2.Response
import retrofit2.http.*


interface AdmittedApi {

    /**用來判斷連線狀況*/
    enum class StatusCode(val code: Int) {
        NORMAL(1),
        SUCCESS(200),
        ERROR_NETWORKNOTCONNECT(-1),
        ERROR(400),

        /**憑證不正確或是過期 請重新登入*/
        ERROR_TOKEN_FAILED(409),

        /**尚未訂閱*/
        ERROR_SUBSCRIBE_FAILED(410),

        /**非預期中的錯誤*/
        ERROR_OTTHER(444)
    }

    companion object {

        const val SERVER_ADDRESS_OFFICAL = "https://user.admitted.tech"
        const val SERVER_ADDRESS_DEV = "https://dev.admitted.tech"
        const val SERVER_COMMON_PREFIX_V1 = "/api/v1/open"
        const val SERVER_COMMON_PREFIX_V2 = "/api/v2/open"

        const val WEB_DETAIL_OFFICAL = ""
        const val WEB_DETAIL_DEV = ""

        const val WEB_DETAIL = WEB_DETAIL_DEV
        // FIXME: 測試完成上架前要改回 SERVER_ADDRESS_OFFICAL
        const val SERVER_ADDRESS = SERVER_ADDRESS_DEV
    }


    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/register/check")
    /**檢查帳號是否能注冊*/
    suspend fun registerCheck(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/register/getcode")
    /**獲取驗證碼*/
    suspend fun registerGetCode(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/register")
    /**註冊*/
    suspend fun register(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/login")
    /**登入*/
    suspend fun login(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/login/google")
    /**登入*/
    suspend fun loginGoogle(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/login/apple")
    /**登入*/
    suspend fun loginApple(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/password/forgot")
    /**忘記密碼*/
    suspend fun forgotPasword(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/password/mod")
    /**修改密碼*/
    suspend fun editPasword(@Field("call") call: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/deviceToken")
    /**登入*/
    suspend fun deviceToken(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/all")
    /**取得全部用戶資料*/
    suspend fun getUserAll(
        @Header("Authorization") token: String,
        @Field("call") call: String,
        @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/basic")
    /**取得基本資料*/
    suspend fun getUserBasic(
        @Header("Authorization") token: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/basic")
    /**編輯基本資料*/
    suspend fun putUserBasic(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/getall")

    /** 取得當前用戶所有資料 */
    suspend fun getAll(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>


    @GET("$SERVER_COMMON_PREFIX_V2/user/academic")
    /**取得在校成績*/
    suspend fun getAcademic(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/academic")
    /**建立在校成績*/
    suspend fun postAcademic(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/academic/{id}")
    /**編輯基本資料*/
    suspend fun putAcademic(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/academic/{id}")
    /**編輯基本資料*/
    suspend fun deleteAcademic(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>


    @GET("$SERVER_COMMON_PREFIX_V2/user/academicYear")
    /**取得在校學年總成績*/
    suspend fun getAcademicyear(
        @Header("Authorization") token: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/academicYear")
    /**建立在校學年總成績*/
    suspend fun postAcademicyear(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/academicYear/{id}")
    /**刪除在校學年總成績*/
    suspend fun putAacademicyear(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/academicYear/{id}")
    /**刪除在校學年總成績*/
    suspend fun deleteAcademicyear(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/intellectualCuriosity")
    /**取得求知欲*/
    suspend fun getIntellectuaCuriosity(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/intellectualCuriosity")
    /**建立求知欲*/
    suspend fun postIntellectuaCuriosity(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/intellectualCuriosity/{id}")
    /**編輯求知欲*/
    suspend fun putIntellectuaCuriosity(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/intellectualCuriosity/{id}")
    /**刪除求知欲*/
    suspend fun deleteIntellectuaCuriosity(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/notification")
    /**取得推播紀錄*/
    suspend fun getNotifications(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/communityImpact")
    /**取得社會影響*/
    suspend fun getCommunityImpact(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/communityImpact")
    /**建立社會影響*/
    suspend fun postCommunityImpact(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/communityImpact/{id}")
    /**編輯社會影響*/
    suspend fun putCommunityImpact(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/communityImpact/{id}")
    /**刪除社會影響*/
    suspend fun deleteCommunityImpact(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/honors")
    /**取得榮譽事蹟*/
    suspend fun getHonors(
        @Header("Authorization") token: String,
        @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/honors")
    /**建立榮譽事蹟*/
    suspend fun postHonors(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/honors/{id}")
    /**編輯榮譽事蹟*/
    suspend fun putHonors(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/honors/{id}")
    /**刪除榮譽事蹟*/
    suspend fun deleteHonors(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/test")
    /**取得測驗成績*/
    suspend fun getTest(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/test")
    /**建立測驗成績*/
    suspend fun postTest(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/test/list")
    /**建立測驗成績*/
    suspend fun postTestLists(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/test/{id}")
    /**編輯測驗成績*/
    suspend fun putTest(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/test/{id}")
    /**刪除測驗成績*/
    suspend fun deleteTest(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>


    @GET("$SERVER_COMMON_PREFIX_V2/user/subscribe")
    /**取得測驗成績*/
    suspend fun getSubscribe(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/subscribe")
    /**建立測驗成績*/
    suspend fun postSubscribe(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/subscribe/{id}")
    /**編輯測驗成績*/
    suspend fun putSubscribe(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/subscribe/{id}")
    /**刪除測驗成績*/
    suspend fun deleteSubscribe(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>


    @GET("$SERVER_COMMON_PREFIX_V2/user/targetSchool")
    /**取得用戶目標學校*/
    suspend fun getUserTargetSchool(
        @Header("Authorization") token: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/targetSchool/list")
    /**建立用戶目標學校*/
    suspend fun postUserTargetSchoolList(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/targetSchool/list")
    /**編輯用戶目標學校*/
    suspend fun putUserTargetSchool(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/targetSchool/deletList")
    /**建立用戶目標學校*/
    suspend fun postDeleteUserTargetSchoolList(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @POST("$SERVER_COMMON_PREFIX_V2/user/targetSchool/{id}")
    /**新增用戶目標學校*/
    suspend fun postUserTargetSchool(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/targetSchool/{id}")
    /**刪除用戶目標學校*/
    suspend fun deleteUserTargetSchool(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>


    @GET("$SERVER_COMMON_PREFIX_V2/targetSchool/brief/list")
    /**取得學校簡章簡易資訊*/
    suspend fun getTargetSchoolBriefList(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/targetSchool/detail/list")
    /**取得學校簡章詳細資訊*/
    suspend fun getTargetSchoolDetailList(
        @Header("Authorization") token: String, @Query("updateTime") updateTIme: String? = null
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/targetSchool/detail/{id}")
    /**取得學校簡章詳細資訊*/
    suspend fun getTargetSchoolDetail(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>


    @GET("$SERVER_COMMON_PREFIX_V2/user/application")
    /**取得用戶申請學校*/
    suspend fun getApplication(
        @Header("Authorization") token: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/user/application")
    /**建立用戶申請學校*/
    suspend fun postApplication(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>


    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/application/{id}")
    /**編輯用戶申請學校*/
    suspend fun putApplication(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/application/{id}")
    /**刪除用戶申請學校*/
    suspend fun deleteApplication(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/user/extracurricular")
    /**取得用戶申請學校*/
    suspend fun getExtracurricular(
        @Header("Authorization") token: String
    ): Response<CommonResponse>
    @FormUrlEncoded

    @POST("$SERVER_COMMON_PREFIX_V2/user/extracurricular")
    /**建立用戶申請學校*/
    suspend fun postExtracurricular(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/user/extracurricular/google/{id}")
    /**刪除用戶申請學校*/
    suspend fun deleteExtracurricular(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>

    /** 設定用戶類型(一次性)
     *
     * 類型 0=學生, 1=兼任家教, 2=家長, 99=尚未選擇
     */
    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/user/type")
    suspend fun putUserType(
        @Header("Authorization") token: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/supervise/students")
    /** 該家長所有的學生資訊
     *
     * 僅提供type=家長時使用，GET應該是取得該家長所有的學生資訊，包含產生該學生的token
     */
    suspend fun getStudentsUnderParent(
        @Header("Authorization") token: String
    ): Response<CommonResponse>

    @GET("$SERVER_COMMON_PREFIX_V2/supervise/student/{id}")
    /** 查詢學生並回傳學生姓名、Logo、系統編號、以及可使用的訂閱資訊。
     *
     * 已經訂閱回傳錯誤。
     */
    suspend fun searchStudent(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Query("method", encoded = true) method: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("$SERVER_COMMON_PREFIX_V2/supervise/student/{id}")
    /** 新增學生到家長/老師底下
     *
     * 檢查訂閱資訊新增訂閲並新增學生
     */
    suspend fun addStudent(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("$SERVER_COMMON_PREFIX_V2/supervise/student/{id}")
    /** 設定/解除目前查看的學生 */
    suspend fun putStudent(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String,
        @Field("call") call: String
    ): Response<CommonResponse>

    @DELETE("$SERVER_COMMON_PREFIX_V2/supervise/student/{id}")
    /** 移除學生
     *
     * 刪除該家長的學生
     */
    suspend fun deleteStudent(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) id: String
    ): Response<CommonResponse>
}