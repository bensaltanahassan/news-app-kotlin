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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.CategoriesAdapter
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.Category
import com.example.newsapp.models.Image
import com.example.newsapp.models.News

class HomeFragment : Fragment() {
    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding!!
    private lateinit var toolbar : Toolbar

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        toolbar = binding.appBarHome.myToolBar
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        categoryRecyclerView = binding.recyclerViewCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        categoryRecyclerView.setHasFixedSize(true)
        getAllCategories()



        newRecyclerView = binding.recyclerViewNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)
        getAllNews()
        return binding.root
    }

    private fun getAllCategories() {
        categoryArrayList = ArrayList<Category>()
        for (
            i in 1..4
        ){
            val imgUrl = "https://res.cloudinary.com/dpj4aia1n/image/upload/v1696587354/v9zcqxggism8ydbkhxlo.jpg"
            val category:Category = Category(
                "$i",
                "Sports",
                "Sports news",
                Image(imgUrl,"1"),
                "2021-04-12T20:00:00.000Z",
                "2021-04-12T20:00:00.000Z",
                0
            )
            categoryArrayList.add(category)
        }
        categoryRecyclerView.adapter  = CategoriesAdapter(
            categoryArrayList,
            {category -> getAllNews()
            },
            null
            )

    }


    private fun getAllNews() {
        newsArrayList = ArrayList<News>()
        for (
            i in 1..4
        ){
            val imgUrl = "https://res.cloudinary.com/dpj4aia1n/image/upload/v1696587354/v9zcqxggism8ydbkhxlo.jpg"
            val news:News = News(
                Image(imgUrl,"1"),
                "Real Madrid win the champions league",
                "Hassan Bensaltana",
                "Real Madrid win the champions league for the 14th time in their history",
                "Sports",
            )
            newsArrayList.add(news)
        }
        newRecyclerView.adapter  = NewsAdapter(newsArrayList,findNavController())
    }

}