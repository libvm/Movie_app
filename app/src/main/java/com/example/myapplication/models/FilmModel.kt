package com.example.myapplication.models


data class FilmModel(
    var fullyLoaded : Boolean = false,
    val id : String,
    var name: String,
    var rating: String = "",
    val year: String,
    val length: String,
    var description: String = "",
    val countries: ArrayList<String>,
    val genres: ArrayList<String>,
    val posterUrl: String,
    val posterUrlPreview: String,
    var favourite: Int = 0
)