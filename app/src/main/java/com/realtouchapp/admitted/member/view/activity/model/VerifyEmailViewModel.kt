package com.realtouchapp.admitted.member.view.activity.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.request.*
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
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
class VerifyEmailViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory) : ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response

    fun register(registeRequest: RegisteRequest) {
        viewModelScope.launch {
            admittedReposritory.register(registeRequest.toJsonEncode()).collect {
                _response.value = it
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

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            admittedReposritory.login(loginRequest.toJsonEncode()).collect {
                _response.value = it
            }
        }
    }
}