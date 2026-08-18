package com.realtouchapp.admitted.begin.viewmodel

import android.util.Log
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
import com.realtouchapp.tools.ext.toJsonEncode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    private val admittedRetrofitRemote: AdmittedRetrofitRemote,
    private val admittedDataStore: AdmittedDataStore,
    val dataBase: AdmittedDataBase
) : ViewModel() {
    companion object {
        private const val TAG = "SplashActivityViewModel"
    }

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response


    private val _userInfo = MutableLiveData<UserInfo>()
    var userInfo = _userInfo
    private val _token = MutableLiveData<String>()
    var token = _token

    private val _response_getall = MutableLiveData<NetworkResult<CommonResponse>>()
    val response_getall = _response_getall


    fun getTargetSchools() {
        viewModelScope.launch {
            admittedReposritory.getTargetSchoolDetailList().collect {
                Log.d(TAG, "getTargetSchools: action=${it?.action} status=${it?.status}")
                _response.value = it
            }
        }
    }
    fun getApplicationSchoolForCheckSubscribel() {
        val runCheckSubscription: () -> Unit = {
            viewModelScope.launch {
                admittedReposritory.getApplicationSchoolForCheckSubscribel().collect {
                    Log.d(TAG, "getApplicationSchoolForCheckSubscribel: action=${it?.action} status=${it?.status}")
                    _response.value = it
                }
            }
        }

        val userType = _observableUserType.value
        when (userType) {
            UserType.TEACHER, UserType.PARENT -> {
                val studentToken = _observableStudentToken.value
                studentToken?.let {
                    runCheckSubscription()
                } ?: run {
                    // 沒有選擇學生，假裝家長已訂閱
                    // FIXME:
                    _response.value = NetworkResult.success(
                        NetworkResult.Action.GET_APPLICATIONSCHHOOL,
                        data = null,
                        statusCode = null
                    )
                }
            }
            else -> runCheckSubscription()
        }
    }

    fun getAllOrStudents() {
        val runGetAll: () -> Unit = {
            viewModelScope.launch {
                admittedReposritory.getAll().collect {
                    Log.d(TAG, "getAllOrStudents: action=${it?.action} status=${it?.status}")
                    _response.value = it
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val userType = loadUserTypeFromLocal()
            when (userType) {
                UserType.TEACHER, UserType.PARENT -> {
                    getStudentToken()?.let {
                        getStudentUserInfo()
                        runGetAll()
                    } ?: run {
                        // 沒有選擇學生，更新家長下的學生清單
                        launch(Dispatchers.Main) {
                            getStudentsUnderParent()
                        }
                    }
                }
                else -> runGetAll()
            }
        }
    }

    private suspend fun getStudentsUnderParent() {
        admittedReposritory.getStudents().collect {
            Log.d(TAG, "getStudentsUnderParent: action=${it?.action} status=${it?.status}")
            _response.value = it
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            admittedReposritory.getUserBasic().collect {
                Log.d(TAG, "getUserInfo: action=${it?.action} status=${it?.status}")
                _response.value = it
            }
        }
    }

    fun getStudentUserInfo() {
        viewModelScope.launch {
            admittedReposritory.getUserBasic(true).collect {
                Log.d(TAG, "getStudentUserInfo: $it")
            }
        }
    }

    private suspend fun loadUserTypeFromLocal(): UserType? {
        val userType = admittedReposritory.keyValueDao.getUserType()
        _observableUserType.postValue(UserType.from(userType))

        return UserType.from(userType)
    }

    private suspend fun getStudentToken(): String? {
        val studentToken = admittedReposritory.keyValueDao.getStudentToken()
        _observableStudentToken.postValue(studentToken)

        return studentToken
    }

    fun getToken() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getToken().let {
                viewModelScope.launch {
                    token.value = it
                }
            }
        }
    }

    fun saveDeviceToken(deviceToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.saveDeviceToken(deviceToken)
        }
    }

    fun postDeviceToken() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getToken()?.let { token ->
                admittedReposritory.keyValueDao.getDeviceToken()?.let { deviceToken ->
                    val result = admittedRetrofitRemote.postDeviceToken(token, DeviceTokenRequest(deviceToken).toJsonEncode())
                }
            }
        }
    }


    private val _isFreeVersion = MutableLiveData(false)
    val isFreeVersion: LiveData<Boolean>
        get() = _isFreeVersion

    fun updateFreeVersion(isFreeVersion: Boolean) {
        viewModelScope.launch {
            admittedDataStore.setFreeVersion(isFreeVersion)
            _isFreeVersion.value = isFreeVersion
        }
    }

    private val _observableUserType = MutableLiveData<UserType?>()
    val observableUserType: LiveData<UserType?>
        get() = _observableUserType

    private val _observableStudentToken = MutableLiveData<String?>()
    val observableStudentToken: LiveData<String?>
        get() = _observableStudentToken

    init {
        // 加载存储的值
        viewModelScope.launch {
            admittedDataStore.isFreeVersion()
                .collect {
                    _isFreeVersion.value = it
                }
        }
    }
}
