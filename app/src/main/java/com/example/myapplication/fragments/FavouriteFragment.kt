package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapters.FilmRecyclerAdapter
import com.example.myapplication.databinding.FragmentFilmlistBinding
import com.example.myapplication.interfaces.RecycleViewOnClickListener
import com.example.myapplication.models.FilmModel
import com.example.myapplication.models.MainViewModel


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
        initSearch()
    }

    private fun initRcView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FilmRecyclerAdapter(this@FavouriteFragment)
        recyclerView.adapter = adapter
    }

    private fun initSearch () {
        val searchView = model.mainFragmentBinding.value?.toolbar?.menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })

        searchView.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(arg0: View) {
                model.mainFragmentBinding.value?.vp?.isUserInputEnabled = true
                model.mainFragmentBinding.value?.tabLayout?.refreshDrawableState()
            }

            override fun onViewAttachedToWindow(arg0: View) {
                model.mainFragmentBinding.value?.vp?.isUserInputEnabled = false
            }
        })
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

    fun filter(text: String) {
        val filteredlist: ArrayList<FilmModel> = ArrayList()
        var currentList = ArrayList<FilmModel>()
        if (model.favouriteDataList.value != null)
            currentList = ArrayList(model.favouriteDataList.value)

        for (item in currentList) {
            if (item.name.contains(text)) {
                filteredlist.add(item)
            }
        }

        adapter.loadList(filteredlist)
    }
}