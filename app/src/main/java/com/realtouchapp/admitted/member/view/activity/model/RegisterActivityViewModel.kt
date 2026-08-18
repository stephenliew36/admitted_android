package com.realtouchapp.admitted.member.view.activity.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.member.view.activity.VerifyEmailActivity
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.request.*
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import com.realtouchapp.tools.ext.toJsonEncode
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
class RegisterActivityViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory,
    private val admittedDataStore: AdmittedDataStore
) : ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response
    private val _responseGoogle = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseGoogle = _responseGoogle

    fun registerCheck(registerCheckRequest: RegisterCheckRequest) {
        viewModelScope.launch {
            admittedReposritory.registerCheck(registerCheckRequest.toJsonEncode())
                .collect { result ->
                    result?.let {
                        when (result.status) {
                            NetworkResult.Status.SUCCESS -> {
                                //成功
                                registerGetCode(RegisteGetcodeRequest(registerCheckRequest.account))
                            }

                            NetworkResult.Status.ERROR -> {
                                //失敗
                                _response.value = it
                            }

                            NetworkResult.Status.LOADING -> {
                                //載入中 可顯示載入動畫
                            }
                        }
                    }
                }
        }
    }

    fun registerGetCode(registeGetcodeRequest: RegisteGetcodeRequest) {
        viewModelScope.launch {
            admittedReposritory.registerGetCode(registeGetcodeRequest.toJsonEncode()).collect {
                _response.value = it
            }
        }
    }
    fun loginGoogle(loginRequest: LoginGoogleRequest) {
        viewModelScope.launch {
            admittedDataStore.saveValue(AdmittedDataStore.KEY_PENDING_GOOGLE_FIRST_NAME, loginRequest.firstName.orEmpty())
            admittedDataStore.saveValue(AdmittedDataStore.KEY_PENDING_GOOGLE_LAST_NAME, loginRequest.lastName.orEmpty())
            admittedReposritory.loginGoogle(loginRequest.toJsonEncode()).collect {
                _responseGoogle.value = it
            }
        }
    }
    fun loginGoogleFailed(){
        viewModelScope.launch {
            _responseGoogle.value = NetworkResult(NetworkResult.Action.LOGIN_GOOGLE, NetworkResult.Status.ERROR, null, null, null,null)
        }
    }
}