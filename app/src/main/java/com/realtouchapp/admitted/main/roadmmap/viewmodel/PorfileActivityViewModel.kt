package com.realtouchapp.admitted.main.roadmmap.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.realtouchapp.admitted.begin.view.SplashActivity
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.UserInfo
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.database.AdmittedDataBase
import com.realtouchapp.admitted.storage.database.DataConverter
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import com.realtouchapp.admitted.storage.datastore.UpdateTimeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/3/14 下午 02:34
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class PorfileActivityViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    private val admittedDataStore: AdmittedDataStore,
    private val dataBase: AdmittedDataBase
) : ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo = _userInfo

    private val _islogout = MutableLiveData<Boolean>()
    val islogout = _islogout

    private val _userType = MutableStateFlow<UserType?>(null)
    val userType: StateFlow<UserType?>
        get() = _userType.asStateFlow()

    fun getUserInfo() {
        viewModelScope.launch {
            admittedReposritory.getUserBasic().collect {
                _response.value = it
            }
        }
    }

    fun getUserInfoCache() {
        viewModelScope.launch (Dispatchers.IO){
            admittedReposritory.cacheUserBasic()?.let {
                viewModelScope.launch {
                    userInfo.value = it
                }
            }
        }
    }

    fun putUserBasic(userInfo: UserInfo) {
        viewModelScope.launch {
            testPrint(TAG = "onDone", msg = "ViewmodelputUserBasic")
            admittedReposritory.putUserBasic(userInfo).collect {
                testPrint(TAG = "onDone", msg = "admittedReposritory.putUserBasic(userInfo).collec")
                _response.value = it
            }
        }
    }

    fun loadUserType() {
        viewModelScope.launch(Dispatchers.IO) {
            val userType = admittedReposritory.keyValueDao.getUserType()
            _userType.emit(UserType.from(userType))
        }
    }

    fun logout(context:Context) {
        viewModelScope.launch(Dispatchers.IO) {
            admittedDataStore.clearAll()
            UpdateTimeStore(context).deleteAll()
            admittedReposritory.deleteAllDao()
            admittedReposritory.keyValueDao.setLogined()
//            context.deleteDatabase(AdmittedDataBase.DATABASE_NAME)
//            context.getDatabasePath(AdmittedDataBase.DATABASE_NAME).delete()

            clearAllTableData()

            _islogout.postValue(true)
        }
    }

    private fun clearAllTableData() {
        dataBase.clearAllTables()
    }

    fun getValue(key:String) = admittedDataStore.getValue(key)
}