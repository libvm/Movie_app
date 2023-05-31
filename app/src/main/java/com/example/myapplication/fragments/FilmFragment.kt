package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFilmBinding
import com.example.myapplication.models.FilmModel
import com.example.myapplication.models.MainViewModel
import com.squareup.picasso.Picasso
import org.json.JSONObject

private const val API_KEY = "3d677980-afbf-49eb-904a-aaf8b9998d52"
private const val Content_Type = "application/json"

class FilmFragment(val filmModel: FilmModel) : Fragment() {
    private lateinit var binding : FragmentFilmBinding
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with (binding) {
        super.onViewCreated(view, savedInstanceState)
        requestFilmData(filmModel.id)
    }

    companion object {
        @JvmStatic
        fun newInstance(filmModel: FilmModel) = FilmFragment(filmModel)
    }

    private fun requestFilmData (filmId : String) {
        if (filmModel.fullyLoaded)
            bindData()
        else {
            val url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/"
            val queue = Volley.newRequestQueue(context)
            val request = object : StringRequest(
                Method.GET, "$url$filmId",
                {
                        result -> loadFilmData(result)
                },
                {
                        error -> Log.d("MyLog", "Error: $error")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-API-KEY"] = API_KEY
                    headers["Content-Type"] = Content_Type
                    return headers
                }
            }
            queue.add(request)
        }
    }
    private fun loadFilmData(data: String) = with (binding){
        val topTemp = ArrayList(model.liveDataList.value)
        val topFilmAt = topTemp.indexOf(filmModel)
        var favTemp = ArrayList<FilmModel>()
        var favFilmAt = -1
        if (model.favouriteDataList.value != null) {
            favTemp =  ArrayList(model.favouriteDataList.value)
            favFilmAt = favTemp.indexOf(filmModel)
        }
        val jsonObj = JSONObject(data)
        filmModel.rating = jsonObj.getString("ratingKinopoisk")
        filmModel.description = jsonObj.getString("description").toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
        filmModel.fullyLoaded = true
        bindData()
        topTemp[topFilmAt] = filmModel
        model.liveDataList.value = topTemp
        if (favFilmAt != -1) {
            favTemp[favFilmAt] = filmModel
            model.favouriteDataList.value = favTemp
        }
    }

    private fun bindData () = with (binding) {
        Picasso.get().load(filmModel.posterUrl).into(filmLogo)
        filmName.text = filmModel.name
        filmDescription.text = filmModel.description

        if (filmModel.rating != "null")
            filmRatingBar.rating = filmModel.rating.toFloat()

        var genres = "Жанры: "
        for (i in 0 until filmModel.genres.size ) {
            genres += if (i != filmModel.genres.size - 1)
                filmModel.genres[i] + ", "
            else filmModel.genres[i]
        }
        filmGenres.text = genres

        var countries = "Страны: "
        for (i in 0 until filmModel.countries.size ) {
            countries += if (i != filmModel.countries.size - 1)
                filmModel.countries[i] + ", "
            else filmModel.countries[i]
        }
        filmCountries.text = countries

        val year = "Год: " + filmModel.year
        filmYear.text = year
    }
}