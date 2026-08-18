package com.realtouchapp.admitted.network.result.base

/**連線狀態用來判斷*/
data class NetworkResult<out T>(val action:Action,val status: Status, val data: T?, val error: CommonResponse?, val message: String?, val statusCode:Int?) {

    //使用動作
    enum class Action {
        LOGIN,
        LOGIN_GOOGLE,
        LOGIN_APPLE,
        FORGOTPASSWORD,
        EDITPASSWORD,
        REGISTER,
        REGISTERCHECK,
        REGISTERGETCODE,
        DEVICE_TOKEN,
        CACHE_USERBASIC,
        GET_USERBASIC,
        PUT_USERBASIC,
        CACHE_HONORS,
        GET_HONORS,
        POST_HONORS,
        PUT_HONORS,
        DELETE_HONORS,
        CACHE_COURSE,
        GET_ALL,
        GET_NOTIFICATION,
        CACHE_NOTIFICATION,
        GET_COURSE,
        POST_COURSE,
        PUT_COURSE,
        DELETE_COURSE,
        CACHE_INTELLECTUALCURIOSITY,
        GET_INTELLECTUALCURIOSITY,
        POST_INTELLECTUALCURIOSITY,
        PUT_INTELLECTUALCURIOSITY,
        DELETE_INTELLECTUALCURIOSITY,
        CACHE_COMMUNITYIMPACT,
        GET_COMMUNITYIMPACT,
        POST_COMMUNITYIMPACT,
        PUT_COMMUNITYIMPACT,
        DELETE_COMMUNITYIMPACT,
        CACHE_TESTS,
        GET_TESTS,
        POST_TESTS,
        POST_TESTLISTS,
        PUT_TESTS,
        DELETE_TESTS,
        CACHE_ACADEMICYEAR,
        GET_ACADEMICYEAR,
        POST_ACADEMICYEAR,
        PUT_ACADEMICYEAR,
        DELETE_ACADEMICYEAR,
        CACHE_ACADEMIC,
        GET_ACADEMIC,
        POST_ACADEMIC,
        PUT_ACADEMIC,
        DELETE_ACADEMIC,
        CACHE_APPLICATIONSCHHOOL,
        GET_APPLICATIONSCHHOOL,
        GET_APPLICATIONSCHHOOL_DETAIL,
        POST_APPLICATIONSCHHOOL,
        PUT_APPLICATIONSCHHOOL,
        DELETE_APPLICATIONSCHHOOL,
        CACHE_USERTARGETSCHHOOL,
        GET_USERTARGETSCHHOOL,
        POST_USERTARGETSCHHOOL,
        PUT_USERTARGETSCHHOOL,
        DELETE_USERTARGETSCHHOOL,
        CACHETARGETSCHHOOL,
        GETTARGETSCHHOOL_BRIEFT,
        GETTARGETSCHHOOL_DETAIL_LIST,
        CACHETARGETSCHHOOL_DETAIL_LIST,
        GETTARGETSCHHOOL_DETAIL_ID,
        GET_SUBSCRIBE,
        POST_SUBSCRIBE,
        PUT_SUBSCRIBE,
        DELETE_SUBSCRIBE,
        PUT_USER_TYPE,
        GET_STUDENTS,
        SEARCH_STUDENT,
        ADD_STUDENT_TO_PARENT,
        PUT_USER_STUDENT,
        DELETE_STUDENT_FROM_PARENT,
    }
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(action: Action,data: T?,statusCode: Int?): NetworkResult<T> {
            return NetworkResult(action,Status.SUCCESS, data, null, null,statusCode)
        }

        fun <T> error(action: Action,message: String, error: CommonResponse?,statusCode: Int?): NetworkResult<T> {
            return NetworkResult(action,Status.ERROR, null, error, message,statusCode)
        }

        fun <T> loading(action: Action,data: T? = null): NetworkResult<T> {
            return NetworkResult(action,Status.LOADING, data, null, null,null)
        }
    }

    override fun toString(): String {
        return "Result(action=$action, status=$status, data=$data, error=$error, message=$message, statusCode=$statusCode)"
    }

}