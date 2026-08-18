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
import javax.inject.Inject

@HiltViewModel
class ParentEditStudentViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory
) : ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response: LiveData<NetworkResult<CommonResponse>>
        get() = _response

    private val _students = MutableLiveData<ArrayList<Student>>()
    val students: LiveData<ArrayList<Student>>
        get() = _students

    fun refreshStudentsFromRemote() {
        viewModelScope.launch {
            admittedReposritory.getStudents().collect {
                _response.value = it

                when (it?.status) {
                    NetworkResult.Status.SUCCESS,
                    NetworkResult.Status.ERROR -> {
                        loadStudentsFromLocal()
                    }
                    else -> { }
                }
            }
        }
    }

    private fun loadStudentsFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            admittedReposritory.deleteStudentUnderParent(student.systemNumber)
                .collect {
                    _response.value = it
                }
        }
    }
}