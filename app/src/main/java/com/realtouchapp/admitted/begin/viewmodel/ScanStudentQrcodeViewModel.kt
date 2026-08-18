package com.realtouchapp.admitted.begin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ScanStudentQrcodeViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory
) : ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response: LiveData<NetworkResult<CommonResponse>>
        get() = _response

    fun searchStudent(keyword: String) {
        viewModelScope.launch {
            admittedReposritory.searchStudent(keyword).collect {
                _response.value = it
            }
        }
    }
}