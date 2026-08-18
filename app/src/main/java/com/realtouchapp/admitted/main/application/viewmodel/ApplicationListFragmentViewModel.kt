package com.realtouchapp.admitted.main.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.UserTargetSchool
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/5/1 上午 06:26
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class ApplicationListFragmentViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory) :
    ViewModel() {

    private val _applicationSchools = MutableLiveData<ArrayList<ApplicationSchool>>()
    val applicationSchools = _applicationSchools

    private val _applicationSchoolsTarget = MutableLiveData<ArrayList<ApplicationSchool>>()
    val applicationSchoolsTarget = _applicationSchoolsTarget

    private val _applicationSchoolsReach = MutableLiveData<ArrayList<ApplicationSchool>>()
    val applicationSchoolsReach = _applicationSchoolsReach

    private val _applicationSchoolsSafety = MutableLiveData<ArrayList<ApplicationSchool>>()
    val applicationSchoolsSafety = _applicationSchoolsSafety
    private val _responseApplicationSchool = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseApplicationSchool = _responseApplicationSchool


    fun postApplicationSchool(applicationSchool: ApplicationSchool){
        viewModelScope.launch() {
            admittedReposritory.postApplicationSchool(applicationSchool).collect{
                responseApplicationSchool.value = it
            }
        }
    }

    fun cacheApplicationSchools(){
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getApplicationSchools()?.let { schools->
                viewModelScope.launch {
                    applicationSchools.value = schools as ArrayList<ApplicationSchool>


                }
            }

        }
    }
}
