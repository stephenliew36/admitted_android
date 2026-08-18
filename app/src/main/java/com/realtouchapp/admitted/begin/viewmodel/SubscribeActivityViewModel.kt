package com.realtouchapp.admitted.begin.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.admitted.network.entity.Subscribe
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.database.AdmittedDataBase
import com.realtouchapp.admitted.storage.datastore.UpdateTimeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SubscribeStudentInfo(
    val student: Student,
    val method: Int,
    val productId: String
): java.io.Serializable

@HiltViewModel
class SubscribeActivityViewModel@Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    private val dataBase: AdmittedDataBase
): ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response

    private val _token = MutableLiveData<String>()
    var token = _token

    var studentInfo: SubscribeStudentInfo? = null

    /** 創建訂閱紀錄 */
    fun postSubscribe(subscribe: Subscribe){
        viewModelScope.launch {
            admittedReposritory.postSubscribe(subscribe).collect {
                _response.value = it
            }
        }

    }

    /** 檢查訂閱資訊新增訂閲並新增學生 */
    fun addStudentAndPostSubscribe(subscribe: Subscribe) {
        if (studentInfo == null) return

        // +002: M0B160001Y2
        // +003: M0B1M0001Y3
        viewModelScope.launch {
            subscribe.method = studentInfo!!.method.toString()
            admittedReposritory.addStudentToParent(
                studentInfo!!.student.systemNumber,
                subscribe
            ).collect {
                _response.value = it
            }
        }
    }

    fun getStudentsUnderParent() {
        viewModelScope.launch {
            admittedReposritory.getStudents().collect {
                _response.value = it
            }
        }
    }

    fun getToken(){
        viewModelScope.launch (Dispatchers.IO){
            admittedReposritory.keyValueDao.getToken().let {
//                testPrint("getToken", it)
                viewModelScope.launch {
                    _token.value =  it
                }
            }
        }

    }
    fun logout(context: Context,method:()->Unit){
//        viewModelScope.launch(Dispatchers.IO) {
        //使用執行去 處理耗時任務
        viewModelScope.launch(Dispatchers.IO) {
            //刪除多個Db 刪除完畢後才回傳
            UpdateTimeStore(context).deleteAll()
            admittedReposritory.deleteAllDao()
//            context.deleteDatabase(AdmittedDataBase.DATABASE_NAME)
//            context.getDatabasePath(AdmittedDataBase.DATABASE_NAME).delete()
            dataBase.clearAllTables()

            withContext(Dispatchers.Main) {
                // 耗時任務完成後執行 finish()
                method()
            }
        }
    }
}