package com.example.myapplication.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.databinding.FragmentMainBinding

class MainViewModel : ViewModel() {
    val liveDataList = MutableLiveData<List<FilmModel>>()
    val favouriteDataList = MutableLiveData<List<FilmModel>>()
    val mainFragmentBinding = MutableLiveData<FragmentMainBinding>()
}