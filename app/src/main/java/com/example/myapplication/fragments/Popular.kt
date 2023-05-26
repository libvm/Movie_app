package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.adapters.FilmAdapter
import com.example.myapplication.adapters.FilmModel
import com.example.myapplication.adapters.RecycleViewOnClickListener
import com.example.myapplication.databinding.FragmentPopularBinding
import org.json.JSONObject
import java.util.Dictionary


private const val API_KEY = "3d677980-afbf-49eb-904a-aaf8b9998d52"
private const val Content_Type = "application/json"

class Popular : Fragment(), RecycleViewOnClickListener {
    private lateinit var binding : FragmentPopularBinding
    private lateinit var adapter : FilmAdapter

    private var pages = 1
    private val model: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataList.observe(viewLifecycleOwner) { // запускает обсервер, который обрабатывает данные,
            adapter.submitList(it)                                         // отправляемые в модель (передает адаптеру)
            adapter.notifyDataSetChanged()
        }
        requestTopListData(pages)
    }

    private fun initRcView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FilmAdapter(this@Popular)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener (object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val filmListSize = model.liveDataList.value?.size
                if (filmListSize != null) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() >= filmListSize - 11) {
                        pages++
                        requestTopListData(pages)
                    }
                }
            }
        })

    }

    private fun requestTopListData(pageNum : Int) { // http запрос
        val url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS"
        val queue = Volley.newRequestQueue(context)
        val request = object: StringRequest(
            Method.GET, "$url&page=$pageNum",
            {
                    result -> loadTopListData(result)
            },
            {
                    error -> Log.d("MyLog","Error: $error")
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

    private fun loadTopListData(data: String) { // парсим и загружаем данные в модель
        var filmList = ArrayList<FilmModel>()
        val temp = model.liveDataList.value
        if (temp != null)
            filmList = ArrayList(temp)
        val jsonObj = JSONObject(data)
        val jsonFilmArray = jsonObj.getJSONArray("films")
        val filmArrayLength = jsonFilmArray.length()
        for (i in 0 until filmArrayLength) {
            val obj = jsonFilmArray[i] as JSONObject
            val jsonCountries = obj.getJSONArray("countries")
            val countriesLength = jsonCountries.length()
            val countries = ArrayList<String>()

            for (j in 0 until countriesLength) {
                val jsonCountry = jsonCountries[j] as JSONObject
                countries.add(jsonCountry.getString("country").toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8))
            }

            val jsonGenres = obj.getJSONArray("genres")
            val genresLength = jsonGenres.length()
            val genres = ArrayList<String>()

            for (j in 0 until genresLength) {
                val jsonGenre = jsonGenres[j] as JSONObject
                val str = jsonGenre.getString("genre").toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
                val char = str[0].uppercaseChar()
                val strCharReplaced = char + str.substring(1)
                genres.add(strCharReplaced)
            }

            val film = FilmModel (
                name = obj.getString("nameRu").toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8),
                year = obj.getString("year"),
                length = obj.getString("filmLength"),
                countries = countries,
                genres = genres,
                posterUrl = obj.getString("posterUrl"),
                posterUrlPreview = obj.getString("posterUrlPreview"),
            )
            filmList.add(film)
        }
        model.liveDataList.value = filmList
    }

    companion object {

        @JvmStatic
        fun newInstance() = Popular()
    }

    override fun onItemClick(pos: Int) {

    }

    override fun onItemLongClick(pos: Int) = with (binding){
        val filmList = ArrayList(model.liveDataList.value)
        if (model.favouriteDataList.value == null) {
            filmList[pos].favourite = R.drawable.star
            model.favouriteDataList.value = List<FilmModel>(1) {
                filmList[pos]
            }
        }
        else {

            val temp = ArrayList(model.favouriteDataList.value)
            filmList[pos].favourite = R.drawable.star
            temp.add(filmList[pos])
            val distincted = temp.distinct()
            model.favouriteDataList.value = distincted
        }
        adapter.notifyDataSetChanged()
    }
}