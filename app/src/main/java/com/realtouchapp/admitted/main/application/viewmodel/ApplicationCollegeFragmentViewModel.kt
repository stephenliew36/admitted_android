package com.realtouchapp.admitted.main.application.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.UserTargetSchool
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/5/1 上午 06:26
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class ApplicationCollegeFragmentViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory
): ViewModel() {

    private val _applicationSchools = MutableLiveData<ArrayList<ApplicationSchool>>()
    val applicationSchools = _applicationSchools

    private val _currentApplicationSchool= MutableLiveData<ApplicationSchool>()
    val currentApplicationSchool = _currentApplicationSchool


    private val _responseApplicationSchool = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseApplicationSchool = _responseApplicationSchool

    private val _responseApplicationSchools = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseApplicationSchools = _responseApplicationSchools

    fun putApplicationSchool(applicationSchool: ApplicationSchool){
        viewModelScope.launch() {
            admittedReposritory.putApplicationSchool(applicationSchool).collect{

                responseApplicationSchool.value = it
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

    fun getApplicationSchool(){
        viewModelScope.launch() {
            admittedReposritory.getApplicationSchool().collect{
                responseApplicationSchools.value = it
            }
        }
    }

    fun cacheApplicationSchool(applicationSchoolId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getApplicationSchools()?.let { schools->
                viewModelScope.launch {
                    for (item in schools){
                        if (item.schoolId == applicationSchoolId){
                            currentApplicationSchool.value = item
                            break
                        }
                    }
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

    private val _userType = MutableStateFlow<UserType?>(null)
    val userType: StateFlow<UserType?>
        get() = _userType.asStateFlow()

    fun loadUserType() {
        viewModelScope.launch(Dispatchers.IO) {
            val userType = admittedReposritory.keyValueDao.getUserType()
            _userType.emit(UserType.from(userType))
        }
    }

    fun isValidDateFormat(dateString: String): Boolean {
        val regex = Regex("\\d{2}/\\d{2}/\\d{4}")
        return regex.matches(dateString)
    }
}
