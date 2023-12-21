package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.example.newsapp.databinding.FragmentArticleDetailsBinding
import com.example.newsapp.models.News

class ArticleDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentArticleDetailsBinding
    private val binding get() = _binding!!

    private lateinit var toolbar : Toolbar
    val args: ArticleDetailsFragmentArgs by navArgs()
    private lateinit var news:News

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_newsdetail, menu)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        news = args.news
        Log.d("New Name",news.title)
        toolbar = binding.appbarNewsDetail.myToolBar
        toolbar.title = "Article Details"
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val ratingBar = binding.ratingBar
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating.toInt()
        }

        return binding.root
    }
}