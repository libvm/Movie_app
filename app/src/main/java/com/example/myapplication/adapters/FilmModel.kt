package com.example.myapplication.adapters

import com.example.myapplication.R

data class FilmModel(
    var name: String,
    var rating: String = "",
    val year: String,
    val length: String,
    var ratingAgeLimits: String = "",
    var description: String = "",
    val countries: ArrayList<String>,
    val genres: ArrayList<String>,
    val posterUrl: String,
    val posterUrlPreview: String,
    var favourite: Int = 0
)