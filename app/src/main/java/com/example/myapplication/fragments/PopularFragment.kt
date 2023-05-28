package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.adapters.FilmRecyclerAdapter
import com.example.myapplication.databinding.FragmentFilmlistBinding
import com.example.myapplication.interfaces.RecycleViewOnClickListener
import com.example.myapplication.models.FilmModel
import com.example.myapplication.models.MainViewModel
import org.json.JSONObject

private const val API_KEY = "3d677980-afbf-49eb-904a-aaf8b9998d52"
private const val Content_Type = "application/json"

class PopularFragment : Fragment(), RecycleViewOnClickListener {
    private lateinit var binding : FragmentFilmlistBinding
    private lateinit var adapter : FilmRecyclerAdapter
    private var pages = 1
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilmlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataList.observe(viewLifecycleOwner) { // запуск обсервера, который обрабатывает данные,
            adapter.submitList(it)                                         // отправляемые в модель
            adapter.notifyDataSetChanged()
        }
        requestTopListData(pages)
    }

    private fun initRcView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FilmRecyclerAdapter(this@PopularFragment)
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
        if (pageNum <= 20) {
            val url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS"
            val queue = Volley.newRequestQueue(context)
            val request = object : StringRequest(
                Method.GET, "$url&page=$pageNum",
                {
                        result -> loadTopListData(result)
                },
                {
                        error -> Log.d("MyLog", "Error: $pageNum")
                }
            )
            {
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

    private fun loadTopListData(data: String) { // парсинг и загрузка данных в модель
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
                val str = jsonCountry.getString("country").toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
                val char = str[0].uppercaseChar()
                val strCharReplaced = char + str.substring(1)
                countries.add(strCharReplaced)
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

            var name = obj.getString("nameRu").toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)
            if (name == "null")
                name = obj.getString("nameEn")

            val film = FilmModel (
                id = obj.getString("filmId"),
                name = name,
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
        fun newInstance() = PopularFragment()
    }

    override fun onItemClick(pos: Int) {
        val temp = ArrayList(model.liveDataList.value)
        parentFragmentManager.beginTransaction()
            .addToBackStack(FilmFragment.newInstance(temp[pos]).javaClass.canonicalName)//optional
            .replace(R.id.placeHolder, FilmFragment.newInstance(temp[pos]))
            .commit()
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
            if (filmList[pos].favourite == 0) {
                filmList[pos].favourite = R.drawable.star
                temp.add(filmList[pos])
                val distincted = temp.distinct()
                model.favouriteDataList.value = distincted
            }
            else {
                val toRemoveAt = temp.indexOf(filmList[pos])
                temp.removeAt(toRemoveAt)
                filmList[pos].favourite = 0
                model.favouriteDataList.value = temp
            }
        }
        adapter.notifyDataSetChanged()
    }
}