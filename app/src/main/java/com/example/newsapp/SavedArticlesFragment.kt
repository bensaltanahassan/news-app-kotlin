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
import com.example.newsapp.databinding.FragmentSavedArticlesBinding
import com.example.newsapp.models.Image
import com.example.newsapp.models.News

class SavedArticlesFragment : Fragment() {

    private lateinit var _binding : FragmentSavedArticlesBinding
    private lateinit var toolbar : Toolbar
    private val binding get() = _binding!!
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<News>


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_home, menu)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentSavedArticlesBinding.inflate(inflater, container, false)
        toolbar = binding.appBarHome.myToolBar
        toolbar.title = "Saved Articles"
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Inside your fragment (e.g., HomeFragment or AccountFragment)
        val bottomNavigationView = binding.bottomNavigationView

        // Determine the current destination
        val currentDestinationId = findNavController().currentDestination?.id

        // Set the default item based on the current destination
        bottomNavigationView.selectedItemId = when (currentDestinationId) {
            R.id.homeFragment -> R.id.home
            R.id.accountFragment -> R.id.settings
            R.id.savedArticlesFragment -> R.id.saved
            else -> R.id.home // Set a default value or handle other cases
        }

        //bottom nav-bar settings
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.saved -> {
                    true
                }
                R.id.home -> {
                    findNavController().navigate(R.id.action_savedArticlesFragment_to_homeFragment)
                    true
                }
                R.id.settings -> {
                    findNavController().navigate(R.id.action_savedArticlesFragment_to_accountFragment)
                    true
                }
                else -> false
            }
        }
        newRecyclerView = binding.recyclerViewSavedNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)

        getAllSavedNews()

        return binding.root
    }
    private fun getAllSavedNews() {
        newArrayList = arrayListOf<News>()
        newRecyclerView.adapter  = NewsAdapter(newArrayList,findNavController(),::onClickMarkButton)
    }

    private fun onClickMarkButton(news: News) {}
}