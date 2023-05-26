package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.adapters.Adapter
import com.example.myapplication.adapters.FilmModel
import com.example.myapplication.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

private const val API_KEY = "3d677980-afbf-49eb-904a-aaf8b9998d52"
private const val Content_Type = "application/json"


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val fList = listOf(
        Popular.newInstance(),
        Favourite.newInstance()
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
    }

    private fun init() = with(binding){
        val firstTabText = getString(R.string.popular)
        val secondTabText = getString(R.string.favourite)
        val adapter = Adapter(activity as FragmentActivity, fList as List<Fragment>)
        vp.adapter = adapter
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