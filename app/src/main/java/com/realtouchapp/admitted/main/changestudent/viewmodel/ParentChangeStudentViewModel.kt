package com.realtouchapp.admitted.main.changestudent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.result.base.CommonResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.whenAllNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentChangeStudentViewModel @Inject constructor(
    private val admittedReposritory: AdmittedReposritory
): ViewModel() {

    private val _response = MutableLiveData<NetworkResult<CommonResponse>>()
    val response: LiveData<NetworkResult<CommonResponse>>
        get() = _response

    private val _students = MutableLiveData<ArrayList<Student>>()
    val students: LiveData<ArrayList<Student>>
        get() = _students

    private val _currentStudent = MutableLiveData<StudentInfo?>()
    val currentStudent: LiveData<StudentInfo?>
        get() = _currentStudent

    fun getUserBasic() {
        viewModelScope.launch {
            admittedReposritory.getUserBasic().collect {
                testPrint(TAG = "onDone", msg = "admittedReposritory.putUserBasic(userInfo).collec")
                _response.value = it
            }
        }
    }

    fun loadStudents() {
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

    fun loadCurrentStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val studentId = admittedReposritory.keyValueDao.getStudentId()
            val studentToken = admittedReposritory.keyValueDao.getStudentToken()
            val studentName = admittedReposritory.keyValueDao.getStudentName()
            listOf(studentId, studentToken, studentName)
                .whenAllNotNull({
                    _currentStudent.postValue(
                        StudentInfo(it[0], it[1], it[2])
                    )
                }, `else` = {
                    _currentStudent.postValue(null)
                })
        }
    }

    fun deselectStudent() {
        viewModelScope.launch {
            _currentStudent.value?.let {
                admittedReposritory.selectStudent(
                    it.id,
                    false
                ).collect {
                    _response.value = it
                }
            }
        }
    }

    fun selectStudent(student: Student) {
        viewModelScope.launch {
            admittedReposritory.selectStudent(
                student.systemNumber,
                true,
                student.token,
                student.firstName ?: ""
            ).collect {
                _response.value = it
            }
        }
    }
}

data class StudentInfo(
    val id: String,
    val token: String,
    val name: String
)