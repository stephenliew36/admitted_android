package com.realtouchapp.admitted.main.explore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollegesFilterBSDailogFragmentViewModel @Inject constructor(private val admittedDataStore: AdmittedDataStore) : ViewModel() {
    private val _isFreeVersion = MutableStateFlow(false)
    val isFreeVersion: StateFlow<Boolean> = _isFreeVersion

    init {
        // 加载存储的值
        viewModelScope.launch {
            admittedDataStore.isFreeVersion().collect {
                _isFreeVersion.value = it
            }
        }
    }
}