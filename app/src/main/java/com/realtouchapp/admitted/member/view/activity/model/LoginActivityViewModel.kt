package com.realtouchapp.admitted.member.view.activity.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.request.LoginGoogleRequest
import com.realtouchapp.admitted.network.request.LoginRequest
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/2/7 上午 02:44
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    private val admittedDataStore: AdmittedDataStore
) : ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            admittedReposritory.login(loginRequest.toJsonEncode()).collect {
                _response.value = it
            }
        }
    }

    fun loginGoogle(loginRequest: LoginGoogleRequest) {
        viewModelScope.launch {
            admittedDataStore.saveValue(AdmittedDataStore.KEY_PENDING_GOOGLE_FIRST_NAME, loginRequest.firstName.orEmpty())
            admittedDataStore.saveValue(AdmittedDataStore.KEY_PENDING_GOOGLE_LAST_NAME, loginRequest.lastName.orEmpty())
            admittedReposritory.loginGoogle(loginRequest.toJsonEncode()).collect {
                _response.value = it
            }
        }
    }

    fun loginApple(loginRequest: LoginGoogleRequest) {
        viewModelScope.launch {
            admittedReposritory.loginApplee(loginRequest.toJsonEncode()).collect {
                _response.value = it
            }
        }
    }
    fun loginGoogleFailed(){
        viewModelScope.launch {
            _response.value = NetworkResult(NetworkResult.Action.LOGIN_GOOGLE, NetworkResult.Status.ERROR, null, null, null,null)
        }
    }
}