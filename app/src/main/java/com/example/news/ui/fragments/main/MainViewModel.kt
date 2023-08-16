package com.example.news.ui.fragments.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.api.TestRepo
import com.example.news.models.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: TestRepo) : ViewModel() {
    private val _all = MutableLiveData<NewsResponse>()
    val all: LiveData<NewsResponse>
        get() = _all

    init {
        getAll()
    }

    fun getAll() = viewModelScope.launch {
        repository.getAll().let {
            if (it.isSuccessful) _all.postValue(it.body())
            else Log.d("check data", "failed to load article ${it.errorBody()}")
        }
    }
}