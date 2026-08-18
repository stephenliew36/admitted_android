package com.realtouchapp.admitted.member.view.activity.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.AcademicYear
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.admitted.network.entity.UserInfo
import com.realtouchapp.admitted.network.entity.UserTargetSchool
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/2/7 上午 02:44
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class BasicInfoActivityViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory,private val admittedDataStore: AdmittedDataStore) : ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response

    fun postData(userInfo: UserInfo,academicYears: ArrayList<AcademicYear>,userTargetSchools: ArrayList<UserTargetSchool>,tests: ArrayList<Test>){
        putUserBasic(userInfo)
        if (userTargetSchools.size>0){
            postUserTargetSchoolList(userTargetSchools)
        }

        for (i in 0 .. (academicYears.size-1)){
            putAcademicYear(academicYears.get(i))
        }

        if (tests.size>0){
            postTestLitsts(tests)
        }

    }

    private fun postUserTargetSchoolList(userTargetSchools: ArrayList<UserTargetSchool>) {
        viewModelScope.launch {
            admittedReposritory.postUserTargetSchoolList(userTargetSchools).collect{
                _response.value = it
            }
        }
    }

    private fun putAcademicYears(academicYears: ArrayList<AcademicYear>) {
        for (i in 0 .. (academicYears.size-1)){
            putAcademicYear(academicYears.get(i))
        }
    }

    private fun putAcademicYear(academicYear: AcademicYear) {
        viewModelScope.launch {
            admittedReposritory.putAcademicYear(academicYear).collect {

                _response.value = it
            }
        }
    }
    private fun putUserBasic(userInfo: UserInfo) {
        viewModelScope.launch {
            admittedReposritory.putUserBasic(userInfo).collect {
                _response.value = it
            }
        }
    }

    private fun postTestLitsts(tests: ArrayList<Test>){

        viewModelScope.launch {
            admittedReposritory.postTestLists(tests).collect {
                _response.value = it
            }
        }
    }

    fun cacheTargetSchoolDetail() {

        viewModelScope.launch {
            admittedReposritory.getFlowTargetSchoolCached().collect {
                _response.value = it
            }
        }
    }

    suspend fun saveKey(key:String,value:String) {
        admittedDataStore.saveValue(key,value)
    }


    fun getValue(key:String) = admittedDataStore.getValue(key)

    suspend fun saveDataClass(key:String,value:Any) {
        admittedDataStore.saveDataClass(key,value)
    }
}