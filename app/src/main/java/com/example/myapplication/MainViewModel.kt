package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.adapters.FilmModel
import java.util.Dictionary

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<FilmModel>()
    val liveDataList = MutableLiveData<List<FilmModel>>()
    val favouriteDataList = MutableLiveData<List<FilmModel>>()
}