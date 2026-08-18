package com.realtouchapp.admitted.begin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.api.AdmittedRetrofitRemote
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.admitted.network.entity.UserInfo
import com.realtouchapp.admitted.network.request.DeviceTokenRequest
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.database.dao.KeyValueDao
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject

@HiltViewModel
class ParentMainViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory
) : ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response: LiveData<NetworkResult<CommonResponse>>
        get() = _response

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    private val _students = MutableLiveData<ArrayList<Student>>()
    val students: LiveData<ArrayList<Student>>
        get() = _students

    fun getUserInfoCache() {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.cacheUserBasic()?.let {
                viewModelScope.launch {
                    _userInfo.value = it
                }
            }
        }
    }

    fun refreshStudentsFromRemote() {
        viewModelScope.launch {
            admittedReposritory.getStudents().collect {
                _response.value = it

                when (it?.status) {
                    NetworkResult.Status.SUCCESS,
                    NetworkResult.Status.ERROR -> {
                        loadStudentsFromLocal(this)
                    }
                    else -> { }
                }
            }
        }
    }

    private fun loadStudentsFromLocal(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.getStudents()?.let {
                _students.postValue(
                    ArrayList<Student>()
                        .apply { addAll(it) }
                )
            } ?: run {
                _students.postValue(arrayListOf())
            }
        }
    }

    fun deleteStudents(students: List<Student>) {
        viewModelScope.launch {
            _response.postValue(
                NetworkResult.loading(NetworkResult.Action.DELETE_STUDENT_FROM_PARENT)
            )

            val deferredList = ArrayList<Deferred<NetworkResult<CommonResponse>?>>()
            for (student in students) {
                val deferred = async {
                    admittedReposritory.deleteStudentUnderParent(
                        student.systemNumber,
                        false
                    ).firstOrNull {
                        it?.status == NetworkResult.Status.SUCCESS
                                || it?.status == NetworkResult.Status.ERROR
                    }
                }
                deferredList.add(deferred)
            }

            deferredList.awaitAll()

            _response.postValue(
                NetworkResult.success(
                    action = NetworkResult.Action.DELETE_STUDENT_FROM_PARENT,
                    data = null,
                    statusCode = 200
                )
            )
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            admittedReposritory.deleteStudentUnderParent(student.systemNumber)
                .collect {
                    _response.value = it
                }
        }
    }

    fun selectStudent(student: Student) {
        viewModelScope.launch {
            admittedReposritory.selectStudent(
                student.systemNumber,
                true,
                student.token,
                "${student.firstName}"
            ).collect {
                _response.value = it
            }
        }
    }
}