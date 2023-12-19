package com.example.newsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.newsapp.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentArticleDetailsBinding
    private val binding get() = _binding!!

    private lateinit var toolbar : Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        val ratingBar = binding.ratingBar
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating.toInt()
        }

        return binding.root
    }
}