package com.example.newsapp

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentAccountBinding
import com.example.newsapp.databinding.FragmentSavedArticlesBinding


class AccountFragment : Fragment() {
    private lateinit var _binding : FragmentAccountBinding
    private lateinit var toolbar : Toolbar
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        toolbar = binding.appBarProfile.myToolBar
        toolbar.title="Profile"

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
                R.id.settings -> {
                    true
                }
                R.id.saved -> {
                    findNavController().navigate(R.id.action_accountFragment_to_savedArticlesFragment)
                    true
                }
                R.id.home -> {
                    findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
                    true
                }
                else -> false
            }
        }


        return binding.root
    }

}