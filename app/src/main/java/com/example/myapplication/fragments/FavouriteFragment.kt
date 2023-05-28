package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.models.MainViewModel
import com.example.myapplication.adapters.FilmRecyclerAdapter
import com.example.myapplication.databinding.FragmentFilmlistBinding
import com.example.myapplication.interfaces.RecycleViewOnClickListener

class FavouriteFragment : Fragment(), RecycleViewOnClickListener {
    private lateinit var binding : FragmentFilmlistBinding
    private lateinit var adapter : FilmRecyclerAdapter
    private val model : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with (binding) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.favouriteDataList.observe(viewLifecycleOwner) {
            val noDuplicates = it.distinct()
            adapter.submitList(noDuplicates)
        }
    }

    private fun initRcView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FilmRecyclerAdapter(this@FavouriteFragment)
        recyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }

    override fun onItemClick(pos: Int) {
        val temp = ArrayList(model.favouriteDataList.value)
        parentFragmentManager.beginTransaction()
            .addToBackStack(FilmFragment.newInstance(temp[pos]).javaClass.canonicalName)//optional
            .replace(R.id.placeHolder, FilmFragment.newInstance(temp[pos]))
            .commit()
    }

    override fun onItemLongClick(pos: Int) {
        val temp = ArrayList(model.favouriteDataList.value)
        val popularTemp = ArrayList(model.liveDataList.value)
        val toReplace = temp[pos]
        val toReplaceAt = popularTemp.indexOf(toReplace)
        toReplace.favourite = 0
        popularTemp[toReplaceAt] = toReplace
        temp.removeAt(pos)
        model.liveDataList.value = popularTemp
        model.favouriteDataList.value = temp
        adapter.notifyDataSetChanged()
    }
}