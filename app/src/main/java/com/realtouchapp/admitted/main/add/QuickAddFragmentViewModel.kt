package com.realtouchapp.admitted.main.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    作者: RobertChou
    時間: 2023/5/22 上午 12:55
    信箱: didi31139@gmail.com
    說明:
*/
@HiltViewModel
class QuickAddFragmentViewModel  @Inject constructor(private val admittedReposritory: AdmittedReposritory) :
    ViewModel() {
    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response = _response


    private val _responseIntellectualCuriosity = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseIntellectualCuriosity = _responseIntellectualCuriosity

    private val _responseCommunityImpact = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseCommunityImpact = _responseCommunityImpact

    private val _responseAcademicYear = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseAcademicYear = _responseAcademicYear

    private val _academicYears = MutableLiveData<ArrayList<AcademicYear>>()
    val academicYears = _academicYears


    private val _responsetestHonors = MutableLiveData<NetworkResult<CommonResponse>>()
    val responsetestHonors = _responsetestHonors

    private val _responseCourse = MutableLiveData<NetworkResult<CommonResponse>>()
    val responseCourse = _responseCourse

    private val _responsetestTest = MutableLiveData<NetworkResult<CommonResponse>>()
    val responsetestTest = _responsetestTest
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo = _userInfo
    fun postCourse(course: Course) {

        viewModelScope.launch {
            admittedReposritory.postCourse(course).collect {
                _responseCourse.value = it
            }
        }
    }

    fun getUserBasic() {

        viewModelScope.launch {
            admittedReposritory.getUserBasic().collect {
                testPrint(TAG = "onDone", msg = "admittedReposritory.putUserBasic(userInfo).collec")
                _response.value = it
            }
        }
    }

    fun getCoursecache() {

        viewModelScope.launch {
            admittedReposritory.getFlowCourseCached().collect {
                _responseCourse.value = it
            }
        }
    }

    fun postIntellectualCuriosity(intellectualCuriosity: IntellectualCuriosity) {

        viewModelScope.launch {
            admittedReposritory.postIntellectualCuriosity(intellectualCuriosity).collect {
                _responseIntellectualCuriosity.value = it
            }
        }
    }

    fun postCommunityImpact(communityImpact: CommunityImpact) {
        viewModelScope.launch {
            admittedReposritory.postCommunityImpact(communityImpact).collect {
                responseCommunityImpact.value = it
            }
        }
    }

    fun postHonors(honors: Honors) {

        viewModelScope.launch {
            admittedReposritory.postHonors(honors).collect {
                responsetestHonors.value = it
            }
        }
    }
    fun postTest(test: Test) {

        viewModelScope.launch {
            admittedReposritory.postTest(test).collect {
                _responsetestTest.value = it
            }
        }
    }
    fun putAcademicYear(academicYear: AcademicYear) {
        viewModelScope.launch {
            admittedReposritory.putAcademicYear(academicYear).collect {

                if (it!!.status == NetworkResult.Status.SUCCESS) {
                    viewModelScope.launch(Dispatchers.IO) {
                        admittedReposritory.keyValueDao.saveAcademicYear(academicYear)
                    }
                }
                responseAcademicYear.value = it
            }
        }
    }

}