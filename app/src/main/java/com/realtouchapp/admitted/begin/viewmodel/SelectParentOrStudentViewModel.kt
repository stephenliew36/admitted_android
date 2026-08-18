package com.realtouchapp.admitted.begin.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.api.AdmittedRetrofitRemote
import com.realtouchapp.admitted.network.entity.UserInfo
import com.realtouchapp.admitted.network.request.DeviceTokenRequest
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.database.AdmittedDataBase
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import com.realtouchapp.admitted.storage.datastore.UpdateTimeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SelectParentOrStudentViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    private val admittedDataStore: AdmittedDataStore,
    private val dataBase: AdmittedDataBase
) : ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response: LiveData<NetworkResult<CommonResponse>>
        get() = _response

    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean>
        get() = _isLogout

    // 當前選擇的用戶類型
    val selectedUserType = MutableLiveData<UserType>()

    fun putUserType() {
        val userType = selectedUserType.value ?: return

        viewModelScope.launch {
            admittedReposritory.putUserType(userType).collect {
                _response.value = it
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            admittedReposritory.getUserBasic().collect {
                _response.value = it
            }
        }
    }


    fun logout(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            admittedDataStore.clearAll()
            UpdateTimeStore(context).deleteAll()
            admittedReposritory.deleteAllDao()
            admittedReposritory.keyValueDao.setLogined()
//            context.deleteDatabase(AdmittedDataBase.DATABASE_NAME)
//            context.getDatabasePath(AdmittedDataBase.DATABASE_NAME).delete()
            dataBase.clearAllTables()

            _isLogout.postValue(true)
        }

    }
}