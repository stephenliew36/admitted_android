package com.realtouchapp.admitted.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.network.result.base.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/2/2 下午 02:43
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class TestViewModel @Inject constructor(private val admittedReposritory:AdmittedReposritory) :
    ViewModel(){
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response
    var userToken  =""

    private val _token = MutableLiveData<String>()
    var token = _token

        


    fun  getToken(){

        viewModelScope.launch(Dispatchers.IO) {

            admittedReposritory.keyValueDao.getToken()?.let { testPrint("getToken", it)
                viewModelScope.launch {
                _token.value =  it
                }
            }
//            userStore.getAccessToken.collect{
//                _token.value = it
//                userToken = it
//            }
        }

    }

    fun deleteHonors(honors: Honors){

        viewModelScope.launch {
            admittedReposritory.deleteHonors(honors).collect {
                _response.value = it
            }
        }
    }
    fun postHonors(honors: Honors){

        viewModelScope.launch {
            admittedReposritory.postHonors(honors).collect {
                _response.value = it
            }
        }
    }
    fun putHonors(honors: Honors){

        viewModelScope.launch {
            admittedReposritory.putHonors(honors).collect {
                _response.value = it
            }
        }
    }
    fun getHonorscache() {

        viewModelScope.launch {
            admittedReposritory.getFlowHonorsCached().collect {
                _response.value = it
            }
        }
    }
    fun getHonors() {

        viewModelScope.launch {
            admittedReposritory.getHonors().collect {
                _response.value = it
            }
        }
    }

    fun login(call:String) {
        viewModelScope.launch {
            admittedReposritory.login(call).collect {
                _response.value = it
            }
        }
    }

    fun deleteTest(test: Test){

        viewModelScope.launch {
            admittedReposritory.deleteTest(test).collect {
                _response.value = it
            }
        }
    }
    fun postTest(test: Test){

        viewModelScope.launch {
            admittedReposritory.postTest(test).collect {
                _response.value = it
            }
        }
    }
    fun putTest(test: Test){

        viewModelScope.launch {
            admittedReposritory.putTest(test).collect {
                _response.value = it
            }
        }
    }
    fun getTestcache() {

        viewModelScope.launch {
            admittedReposritory.getFlowTestCached().collect {
                _response.value = it
            }
        }
    }
    fun getTest() {

        viewModelScope.launch {
            admittedReposritory.getTest().collect {
                _response.value = it
            }
        }
    }
    fun deleteCourse(course: Course){

        viewModelScope.launch {
            admittedReposritory.deleteCourse(course).collect {
                _response.value = it
            }
        }
    }

    fun postCourse(course: Course){

        viewModelScope.launch {
            admittedReposritory.postCourse(course).collect {
                _response.value = it
            }
        }
    }
    fun putCourse(course: Course){

        viewModelScope.launch {
            admittedReposritory.putCourse(course).collect {
                _response.value = it
            }
        }
    }
    fun getCoursecache() {

        viewModelScope.launch {
            admittedReposritory.getFlowCourseCached().collect {
                _response.value = it
            }
        }
    }
    fun getCourse() {

        viewModelScope.launch {
            admittedReposritory.getCourse().collect {
                _response.value = it
            }
        }
    }

    fun deleteIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity){

        viewModelScope.launch {
            admittedReposritory.deleteIntellectualCuriosity(intellectualCuriosity).collect {
                _response.value = it
            }
        }
    }
    fun postIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity){

        viewModelScope.launch {
            admittedReposritory.postIntellectualCuriosity(intellectualCuriosity).collect {
                _response.value = it
            }
        }
    }
    fun putIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity){

        viewModelScope.launch {
            admittedReposritory.putIntellectualCuriosity(intellectualCuriosity).collect {
                _response.value = it
            }
        }
    }
    fun getIntellectualCuriositycache() {

        viewModelScope.launch {
            admittedReposritory.getFlowIntellectualCuriosityCached().collect {
                _response.value = it
            }
        }
    }
    fun getIntellectualCuriosity() {

        viewModelScope.launch {
            admittedReposritory.getIntellectualCuriosity().collect {
                _response.value = it
            }
        }
    }

    fun deleteCommunityImpact(communityImpact: CommunityImpact){

        viewModelScope.launch {
            admittedReposritory.deleteCommunityImpact(communityImpact).collect {
                _response.value = it
            }
        }
    }

    fun postCommunityImpact(communityImpact: CommunityImpact){

        viewModelScope.launch {
            admittedReposritory.postCommunityImpact(communityImpact).collect {
                _response.value = it
            }
        }
    }
    fun putCommunityImpact(communityImpact: CommunityImpact){

        viewModelScope.launch {
            admittedReposritory.putCommunityImpact(communityImpact).collect {
                _response.value = it
            }
        }
    }
    fun getCommunityImpactcache() {

        viewModelScope.launch {
            admittedReposritory.getFlowCommunityImpactCached().collect {
                _response.value = it
            }
        }
    }
    fun getCommunityImpact() {

        viewModelScope.launch {
            admittedReposritory.getCommunityImpact().collect {
                _response.value = it
            }
        }
    }


    fun putUserInfo(honors: UserInfo){

        viewModelScope.launch {
            admittedReposritory.putUserBasic(honors).collect {
                _response.value = it
            }
        }
    }
    fun getUserInfocache() {

        viewModelScope.launch {
            admittedReposritory.getFlowUserBasic().collect {
                _response.value = it
            }
        }
    }
    fun getUserInfo() {

        viewModelScope.launch {
            admittedReposritory.getUserBasic().collect {
                _response.value = it
            }
        }
    }

    fun getUserTargetSchools(){
        viewModelScope.launch {
            admittedReposritory.getUserTargetSchool().collect {
                _response.value = it
            }
        }
    }
    fun getTargetSchoolDetail() {

        viewModelScope.launch {
            admittedReposritory.getTargetSchoolDetailList().collect {
                _response.value = it
            }
        }
    }
    fun getTargetSchoolBriefList() {

        viewModelScope.launch {
            admittedReposritory.getTargetSchoolBriefList().collect {
                _response.value = it
            }
        }
    }

    fun getTargetSchoolDetail(id:String) {

        viewModelScope.launch {
            admittedReposritory.getTargetSchoolDetail(id).collect {
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

    fun cacheUserTargetSchool() {

        viewModelScope.launch(Dispatchers.IO) {

            var result= NetworkResult.success(NetworkResult.Action.CACHE_USERTARGETSCHHOOL,CommonResponse("","",admittedReposritory.keyValueDao.getUserTargetschools()),200)
            viewModelScope.launch {
                _response.value = result
            }
        }
    }
    private fun putUserTargetSchool(userTargetSchools: ArrayList<UserTargetSchool>) {
        viewModelScope.launch {
            admittedReposritory.putUserTargetSchool(userTargetSchools)
        }
    }
}