package com.example.myapplication.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val liveDataList = MutableLiveData<List<FilmModel>>()
    val favouriteDataList = MutableLiveData<List<FilmModel>>()
}