package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.adapters.FilmAdapter
import com.example.myapplication.adapters.FilmModel
import com.example.myapplication.adapters.RecycleViewOnClickListener
import com.example.myapplication.databinding.FragmentFavouriteBinding

class Favourite : Fragment(), RecycleViewOnClickListener {
    private lateinit var binding : FragmentFavouriteBinding
    private lateinit var adapter : FilmAdapter
    private var favouriteFilmList = ArrayList<FilmModel>()
    private val model : MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with (binding) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.favouriteDataList.observe(viewLifecycleOwner) {
            adapter.submitList(favouriteFilmList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initRcView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FilmAdapter(this@Favourite)
        recyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = Favourite()
    }

    override fun onItemClick(pos: Int) {

    }
}