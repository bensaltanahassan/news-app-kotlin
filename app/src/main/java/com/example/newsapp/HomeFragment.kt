package com.example.newsapp

import Crud
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.CategoriesAdapter
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.data.HomeData
import com.example.newsapp.data.NewsData
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.Category
import com.example.newsapp.models.Image
import com.example.newsapp.models.News
import com.example.newsapp.utlis.ResponseHomeData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class HomeFragment : Fragment() {
    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding!!
    private lateinit var toolbar : Toolbar
    private val homeData:HomeData  = HomeData()
    private val newsData:NewsData  = NewsData()

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newsArrayList: ArrayList<News>

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryArrayList: ArrayList<Category>



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_home, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newsArrayList = ArrayList<News>()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        toolbar = binding.appBarHome.myToolBar
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val searchView:EditText = binding.appBarHome.searchNewsHome
        val backButton:View = binding.appBarHome.backButtonHome

        backButton.setOnClickListener {
            searchView.visibility = View.GONE
            toolbar.title = "News App"
            toolbar.menu.findItem(R.id.action_search).isVisible = true
            backButton.visibility = View.GONE
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_search -> {
                    toolbar.title = ""
                    searchView.visibility = View.VISIBLE
                    toolbar.menu.findItem(R.id.action_search).isVisible = false
                    backButton.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        categoryRecyclerView = binding.recyclerViewCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        categoryRecyclerView.setHasFixedSize(true)

        newRecyclerView = binding.recyclerViewNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)


        getHomeData()
        return binding.root
    }



    private fun getHomeData(){
        binding.progressBarHome.visibility = View.VISIBLE
        binding.recyclerViewNews.visibility = View.GONE
        binding.recyclerViewCategory.visibility = View.GONE

        homeData.getHomeData(
            onSuccess = { responseHomeData ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.VISIBLE
                    binding.recyclerViewCategory.visibility = View.VISIBLE
                    getAllCategories(responseHomeData.data.categories)
                    getAllNews(responseHomeData.data.news)
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.GONE
                    binding.recyclerViewCategory.visibility = View.GONE
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )

    }



    private fun getAllCategories(listCategory:List<Category>) {
        categoryArrayList = ArrayList<Category>()
        listCategory.forEach { categoryArrayList.add(it) }
        categoryRecyclerView.adapter  = CategoriesAdapter(
            categoryArrayList,
            fun(category: Category) {
                getNewsByCategory(category)
            },
            null
            )
    }

    private fun getNewsByCategory(category: Category){
        binding.progressBarHome.visibility = View.VISIBLE
        binding.recyclerViewNews.visibility = View.GONE


        newsData.getNewsByCategory(category,
            onSuccess = { responseNewsData ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.VISIBLE



                    getAllNews(responseNewsData.data)
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


    private fun getAllNews(listNews:List<News>) {
        requireActivity().runOnUiThread {
            newsArrayList.clear()
            listNews.forEach { newsArrayList.add(it) }
            if (newRecyclerView.adapter == null) {
                newRecyclerView.adapter = NewsAdapter(newsArrayList, findNavController())
            } else {
                newRecyclerView.adapter?.notifyDataSetChanged()
            }
            if (listNews.isNotEmpty()) {
                binding.recyclerViewNews.visibility = View.VISIBLE
            } else {
                Toast.makeText(context, "No news available", Toast.LENGTH_SHORT).show()
                binding.recyclerViewNews.visibility = View.GONE
            }
        }

    }

}