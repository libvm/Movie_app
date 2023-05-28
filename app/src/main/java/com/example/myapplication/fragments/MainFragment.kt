package com.example.myapplication.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.R
import com.example.myapplication.adapters.MainFragmentAdapter
import com.example.myapplication.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val fList = listOf(
        PopularFragment.newInstance(),
        FavouriteFragment.newInstance()
    )
    private val tList = listOf(
        "Популярные",
        "Избранное"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        val searchView = getView()?.findViewById<ActionMenuItemView>(R.id.action_search)
        searchView?.setBackgroundColor(Color.GREEN)
    }

    private fun init() = with(binding){
        val firstTabText = getString(R.string.popular)
        val secondTabText = getString(R.string.favourite)
        val mainFragmentAdapter = MainFragmentAdapter(activity as FragmentActivity, fList as List<Fragment>)
        vp.adapter = mainFragmentAdapter
        TabLayoutMediator(tabLayout, vp) {
            tab, pos -> tab.text = tList[pos]
        }.attach()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == firstTabText) textView.text = firstTabText
                else textView.text = secondTabText
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}