package com.realtouchapp.admitted.member.view.activity.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/5/21 下午 08:18
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class NotificaitonActivityViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory) : ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response

    fun getNotificiations(){
        viewModelScope.launch {
            admittedReposritory.getFcmMessage().collect{
                _response.value = it
            }
        }

    }

}