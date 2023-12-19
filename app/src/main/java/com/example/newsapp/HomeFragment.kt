package com.example.newsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentChangePasswordBinding
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.models.New

class HomeFragment : Fragment() {
    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding!!
    private lateinit var toolbar : Toolbar

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<New>
    lateinit var imageIds : Array<Int>
    lateinit var headings : Array<String>


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_home, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        toolbar = binding.appBarHome.myToolBar
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        imageIds = arrayOf(
            R.drawable.sport,
            R.drawable.sport,
            R.drawable.sport,
            R.drawable.sport,
            R.drawable.sport,
            R.drawable.sport,
        )
        headings= arrayOf(
            "Hasazd cazcazcaz",
            "Hasazd cazcazcaz",
            "Hasazd cazcazcaz",
            "Hasazd cazcazcaz",
            "Hasazd cazcazcaz",
            "Hasazd cazcazcaz",
        )
        newRecyclerView = binding.recyclerViewNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<New>()
        getAllNews()
        return binding.root
    }


    private fun getAllNews() {
        for (i in imageIds.indices){
            val news = New(imageIds[0],headings[0])
            newArrayList.add(news)
        }
        newRecyclerView.adapter  = NewsAdapter(newArrayList)
    }

}