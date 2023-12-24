package com.example.newsapp

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.CategoriesAdapter
import com.example.newsapp.adapters.FavorisAdapter
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.data.FavorisData
import com.example.newsapp.databinding.FragmentSavedArticlesBinding
import com.example.newsapp.models.Favoris
import com.example.newsapp.models.Image
import com.example.newsapp.models.News
import com.example.newsapp.models.User

class SavedArticlesFragment : Fragment() {

    private lateinit var _binding : FragmentSavedArticlesBinding
    private lateinit var toolbar : Toolbar
    private val binding get() = _binding!!
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<News>
    private lateinit var favorisData: FavorisData
    private lateinit var favorisArrayList : ArrayList<Favoris>
    private lateinit var user: User
    private lateinit var sharedPref: SharedPreferencesManager


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

        //get the user from sharedPrefs
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!
        //construct favorisData
        favorisData = FavorisData(user._id,user.token!!)
        //get all saved news
        getAllSavedNews()

        newRecyclerView = binding.recyclerViewSavedNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)



        return binding.root
    }
    private fun getAllSavedNews() {
        newArrayList = arrayListOf<News>()
        favorisArrayList = arrayListOf<Favoris>()
        favorisData.getAllFavoris( onSuccess = { response ->
            requireActivity().runOnUiThread {
                binding.progressBarHome.visibility= View.GONE
                binding.recyclerViewSavedNews.visibility = View.VISIBLE
                for (item in response.data){
                    newArrayList.add(item.article)
                    favorisArrayList.add(item)
                }
                newArrayList.forEach{
                    it.isFavorite=true
                }
                if (binding.recyclerViewSavedNews.adapter == null) {
                    binding.recyclerViewSavedNews.adapter = FavorisAdapter(
                        newArrayList,
                        findNavController(),
                        ::onClickMarkButton
                    )
                } else {
                    binding.recyclerViewSavedNews.adapter?.notifyDataSetChanged()
                }
                if (newArrayList.isNotEmpty()) {
                    binding.recyclerViewSavedNews.visibility = View.VISIBLE
                    binding.noNewsLayout.visibility = View.GONE
                } else {
                    binding.noNewsLayout.visibility = View.VISIBLE
                    binding.recyclerViewSavedNews.visibility = View.GONE
                }

            }
        }, onFailure = { message ->
            Log.d("Error", message)
        })

    }

    private fun onClickMarkButton(news: News) {
        val favoris = favorisArrayList.find { it.article._id == news._id }
        if (favoris != null) {
            favorisData.deleteFromFavoris(favoris, onSuccess = { response ->
                val new = newArrayList.find { it._id == news._id }
                newArrayList.remove(new)
                requireActivity().runOnUiThread {
                    getAllSavedNews()
                    Toast.makeText(context, "Supprimé avec succes from favoris", Toast.LENGTH_SHORT).show()
                }

            }, onFailure = { message ->
                Log.d("Error", message)
            })
        }

    }


}