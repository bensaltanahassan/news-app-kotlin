package com.example.newsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
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


        return binding.root
    }

}