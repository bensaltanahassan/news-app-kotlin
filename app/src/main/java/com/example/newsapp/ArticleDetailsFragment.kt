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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.newsapp.databinding.FragmentArticleDetailsBinding
import com.example.newsapp.models.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

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
        toolbar = binding.appbarNewsDetail.myToolBar
        toolbar.title = news.categoryId.name
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        binding.articleAuthor.text = news.author
        binding.articleName.text = news.title
        binding.articleContent.text = news.content
        binding.articleDate.text = formatDate(news.createdAt)
        //load image
        val request = coil.request.ImageRequest.Builder(binding.imageArticleDetails.context)
            .data(news.image.url)
            .target(binding.imageArticleDetails)
            .target(
                onStart = {
                    binding.progressBarNewsDetail.visibility = View.VISIBLE
                },
                onSuccess = { result ->
                    binding.progressBarNewsDetail.visibility = View.GONE
                    binding.imageArticleDetails.visibility = View.VISIBLE
                    binding.imageArticleDetails.setImageDrawable(result)
                    Log.d("succes","succes")
                },
                onError = { _ ->
                    binding.progressBarNewsDetail.visibility = View.GONE
                    binding.imageArticleDetails.visibility = View.VISIBLE
                    Log.d("error","error in loading image")
                }
            ).build()
        coil.ImageLoader(binding.imageArticleDetails.context).enqueue(request)

        val ratingBar = binding.ratingBar
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingBar.rating.toInt()
        }


        return binding.root
    }
    fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date: Date = inputFormat.parse(date)!!
        val formattedDate:String = outputFormat.format(date)
        return formattedDate
    }


}