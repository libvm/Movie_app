package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.adapters.MainFragmentAdapter
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.models.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val model : MainViewModel by activityViewModels()

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
        model.mainFragmentBinding.value = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() = with(binding){
        val mainFragmentAdapter = MainFragmentAdapter(activity as FragmentActivity, fList as List<Fragment>)
        vp.adapter = mainFragmentAdapter
        TabLayoutMediator(tabLayout, vp) {
            tab, pos -> tab.text = tList[pos]
        }.attach()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "Популярные") {
                    textView.text = "Популярные"
                }
                else {
                    textView.text = "Избранное"
                }
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