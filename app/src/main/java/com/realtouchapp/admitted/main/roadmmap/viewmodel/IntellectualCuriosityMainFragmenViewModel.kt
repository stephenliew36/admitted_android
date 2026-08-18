package com.realtouchapp.admitted.main.roadmmap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/2/13 上午 11:17
    信箱: didi31139@gmail.com
    說明:
*/
class IntellectualCuriosityMainFragmenViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory) : ViewModel(){
    private val _responseIntellectualCuriosit = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseIntellectualCuriosit = _responseIntellectualCuriosit



}