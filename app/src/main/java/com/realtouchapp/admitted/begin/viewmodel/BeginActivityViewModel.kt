package com.realtouchapp.admitted.begin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.network.api.AdmittedReposritory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeginActivityViewModel @Inject constructor(private val admittedReposritory: AdmittedReposritory) :
    ViewModel() {


    fun isLogined(method:()->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            admittedReposritory.keyValueDao.isLogined().let {
                viewModelScope.launch {
                    if (it){
                    }else{
                        method()
                    }

                }
            }
        }
    }

}