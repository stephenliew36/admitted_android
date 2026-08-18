package com.realtouchapp.admitted.main.explore.viewmodel

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/3/23 上午 01:34
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class SchoolMainFragmentViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory) : ViewModel(){

//    private val _categoryTargetSchools = MutableLiveData<ArrayList<HashMap<String, ArrayList<TargetSchool>>>>()
//    val categoryTargetSchools = _categoryTargetSchools
//

    private val _userTargetSchools = MutableLiveData<ArrayList<UserTargetSchool>>()
    val userTargetSchools = _userTargetSchools

    private val _applicationSchools = MutableLiveData<ArrayList<ApplicationSchool>>()
    val applicationSchools = _applicationSchools


    private val _responseUserTargetSchool = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseUserTargetSchool= _responseUserTargetSchool

    private val _responseApplicationSchool = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseApplicationSchool = _responseApplicationSchool

    fun postUserTargetSchool(userTargetSchool: UserTargetSchool){
        viewModelScope.launch {
            admittedReposritory.postUserTargetSchool(userTargetSchool).collect{
                responseUserTargetSchool.value = it
            }
        }
    }

    fun deleteUserTargetSchool(userTargetSchool: UserTargetSchool){
        viewModelScope.launch {
            admittedReposritory.deleteUserTargetSchool(userTargetSchool).collect{
                responseUserTargetSchool.value= it
            }
        }
    }

    fun postApplicationSchool(applicationSchool: ApplicationSchool){
        viewModelScope.launch() {
            admittedReposritory.postApplicationSchool(applicationSchool).collect{
                responseApplicationSchool.value = it
            }
        }
    }

    fun cacheUserTargeSchools(){
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getUserTargetschools()?.let { schools->
                viewModelScope.launch {
                    userTargetSchools.value = schools as ArrayList<UserTargetSchool>
                }
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
