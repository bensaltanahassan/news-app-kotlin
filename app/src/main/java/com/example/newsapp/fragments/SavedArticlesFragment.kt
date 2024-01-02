package com.example.newsapp.fragments

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.FavorisAdapter
import com.example.newsapp.data.FavorisData
import com.example.newsapp.databinding.FragmentSavedArticlesBinding
import com.example.newsapp.models.Favoris
import com.example.newsapp.models.News
import com.example.newsapp.models.User
import com.google.android.material.divider.MaterialDividerItemDecoration
import io.getstream.avatarview.coil.loadImage

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


    //drawer
    private lateinit var toggle:ActionBarDrawerToggle



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_home, menu)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //get the user from sharedPrefs
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!


       _binding = FragmentSavedArticlesBinding.inflate(inflater, container, false)
        toolbar = binding.appBarHome.myToolBar
        toolbar.title = "Saved Articles"
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)




        //drawer menu
        toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setCheckedItem(R.id.savedNewsPageDrawer)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nameHeaderMenu).text = user.firstName + " " + user.lastName




        //access to the image in the header in the drawer
        val headerView: View = binding.navView.getHeaderView(0)
        val avatarHeaderDrawer = headerView.findViewById<io.getstream.avatarview.AvatarView>(R.id.avatarViewHeader)
        val progressBarImageHeader = headerView.findViewById<ProgressBar>(R.id.progressBarImageHeader)
        avatarHeaderDrawer.loadImage(
            user.profilePhoto.url,
            onStart = {
                progressBarImageHeader.visibility = View.VISIBLE
                avatarHeaderDrawer.visibility = View.GONE
            },
            onComplete = {
                progressBarImageHeader.visibility = View.GONE
                avatarHeaderDrawer.visibility = View.VISIBLE
            },
            onError = { _, _ ->
                avatarHeaderDrawer.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.baseline_error_outline_24
                ))
                progressBarImageHeader.visibility = View.GONE
                avatarHeaderDrawer.visibility = View.VISIBLE
            },
        )
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homePageDrawer -> {
                    findNavController().navigate(R.id.action_savedArticlesFragment_to_homeFragment)
                    true
                }
                R.id.profilePageDrawer -> {
                    findNavController().navigate(R.id.action_savedArticlesFragment_to_accountFragment)
                    true
                }
                R.id.logOutDrawer -> {
                    sharedPref.logout()
                    findNavController().navigate(R.id.action_savedArticlesFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }





        // bottom nav bar
        val bottomNavigationView = binding.bottomNavigationView

        val currentDestinationId = findNavController().currentDestination?.id

        bottomNavigationView.selectedItemId = when (currentDestinationId) {
            R.id.homeFragment -> R.id.home
            R.id.accountFragment -> R.id.settings
            R.id.savedArticlesFragment -> R.id.saved
            else -> R.id.home
        }

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
        // end bottom nav bar


        //construct favorisData
        favorisData = FavorisData(user._id,user.token!!)


        newArrayList = arrayListOf<News>()
        favorisArrayList = arrayListOf<Favoris>()
        //get all saved news
        getAllSavedNews()

        newRecyclerView = binding.recyclerViewSavedNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)



        return binding.root
    }
    private fun loadSavedNews() {
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
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
                    binding.recyclerViewSavedNews.addItemDecoration(divider)

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
                requireActivity().runOnUiThread {
                    // Remove the deleted item from the ArrayList
                    favorisArrayList.remove(favoris)
                    newArrayList.remove(news)
                    getAllSavedNews()
                    Toast.makeText(context, "Supprimé avec succes de favoris", Toast.LENGTH_SHORT).show()
                }

            }, onFailure = { message ->
                Log.d("Error", message)
            })
        }

    }
    private fun getAllSavedNews() {
        // Clear existing data
        newArrayList.clear()
        favorisArrayList.clear()

        // Reload saved news
        loadSavedNews()
    }


}