package com.realtouchapp.admitted.ext

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import retrofit2.HttpException
import kotlin.math.roundToInt

/*
    作者: RobertChou
    時間: 2023/1/3 下午 05:20
    信箱: didi31139@gmail.com
    說明:
*/
fun showNetwrokNotConncet(binding_root: View){
    Snackbar.make(binding_root, "Connection failed. Please check your connection status.", Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
    }.show()
}

fun showSnackBar(binding_root: View,text:String){
    Snackbar.make(binding_root, text, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
    }.show()
}
fun showProgressDialog(activity: Activity): Dialog {
    return ProgressDialog.show(activity, "", "載入中請稍後", true)
}


suspend fun netWorkResponse(callMeathod: suspend() -> Unit,httpError: (code:Int,errorBody:String) -> Unit) {
    try {
        callMeathod()
    }catch (exception: HttpException){
        var code = exception.response()?.code()
        val rp:String = exception.response()?.errorBody()!!.string()
        httpError(code!!,rp)
    }

}
fun closeSoftIntPut(view: View){
    val inputMethodManager = view.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)

}
// Server 偶爾會在 Int 欄位回傳小數（例：targetSchools[0].male = 49.8），
// 預設 Gson 解析會丟出 JsonSyntaxException 導致 App 啟動閃退。
// 這裡註冊寬鬆的 Int TypeAdapter，遇到小數時四捨五入。
private val lenientIntTypeAdapter = object : TypeAdapter<Int>() {
    override fun write(out: JsonWriter, value: Int?) {
        if (value == null) out.nullValue() else out.value(value)
    }

    override fun read(reader: JsonReader): Int? {
        return when (reader.peek()) {
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }
            JsonToken.STRING -> reader.nextString().toDoubleOrNull()?.roundToInt()
            else -> reader.nextDouble().roundToInt()
        }
    }
}

val admittedGson: Gson = GsonBuilder()
    .registerTypeAdapter(Int::class.javaObjectType, lenientIntTypeAdapter)
    .registerTypeAdapter(Int::class.javaPrimitiveType, lenientIntTypeAdapter)
    .create()

fun deCryption(message:String):DeCryptionResponse{
   var response =  admittedGson.fromJson(
        AdmittedEncrption().deCryption(message),
        DeCryptionResponse::class.java)
    testPrint("deCryption",message)

    testPrint("deCryption",GsonBuilder().setPrettyPrinting().create().toJson(response))
    return response
}


