package com.realtouchapp.admitted.network.result.base


data class CommonResponse (
    //成功"success",失敗"error"
    val status: String = "error",
    //加密回傳
    val message: String? = null,
    var data: Any? = null
    ){

}