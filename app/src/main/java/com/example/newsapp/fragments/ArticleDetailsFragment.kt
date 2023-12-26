package com.example.newsapp.fragments

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
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.data.NewsData
import com.example.newsapp.data.NewsDetailsData
import com.example.newsapp.data.RatingData
import com.example.newsapp.databinding.FragmentArticleDetailsBinding
import com.example.newsapp.models.News
import com.example.newsapp.models.Rate
import com.example.newsapp.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentArticleDetailsBinding
    private val binding get() = _binding!!

    private lateinit var toolbar : Toolbar
    val args: ArticleDetailsFragmentArgs by navArgs()
    private lateinit var news:News
    private final lateinit var rateData:RatingData
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    private lateinit var userId:String
    private var token:String? = null
    private lateinit var newsData: NewsData
    private var avgRating:Double=0.0
    private lateinit var newsDetails : News



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_newsdetail, menu)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Shared preferences
        sharedPref = SharedPreferencesManager.getInstance(requireContext())

        user = sharedPref.getUser()!!
        userId = user._id
        token = user.token
        rateData = RatingData(userId,token!!)
        newsData = NewsData(userId,token!!)


        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        news = args.news
        toolbar = binding.appbarNewsDetail.myToolBar
        toolbar.title = news.categoryId.name
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)





        //set data
        getSingleNewsDetails()


        //handlingRating
        val ratingBar = binding.ratingBar
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            handleRating(newsDetails._id,ratingBar.rating.toInt())
        }
        getSingleNewsDetails()

        return binding.root
    }
    fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date: Date = inputFormat.parse(date)!!
        val formattedDate:String = outputFormat.format(date)
        return formattedDate
    }
    fun handleRating (articleId:String, rating:Int){
        rateData.handleRating(articleId,rating, onSuccess = {responseRateData ->
        }, onFailure = {
            Toast.makeText(requireContext(),"Erreur lors de l'ajout de l'avis",Toast.LENGTH_SHORT).show()
        })
    }
    private fun getSingleNewsDetails(){
        newsData.getSingleNews(
            news._id,
            onSuccess = {getSingleNewsResponse ->
            newsDetails = getSingleNewsResponse.data.article
            val rating = getSingleNewsResponse.data.rating?.rating?.toFloat() ?: 0f
            avgRating = getSingleNewsResponse.data.avgRating?.toDouble() ?: 0.0
            Log.d("newsDetails",getSingleNewsResponse.toString())
            requireActivity().runOnUiThread(){
                //set binding
                binding.ratingBar.visibility = View.VISIBLE
                binding.ratingId.visibility = View.VISIBLE
                binding.articleAuthor.text = newsDetails.author
                binding.articleName.text = newsDetails.title
                binding.articleContent.text = newsDetails.content
                binding.articleDate.text = formatDate(newsDetails.createdAt)
                //load image
                val request = coil.request.ImageRequest.Builder(binding.imageArticleDetails.context)
                    .data(newsDetails.image.url)
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
                //TODO: set rating bug
                binding.ratingBar.rating = rating.toFloat()
                binding.ratingId.text = avgRating.toString()


            }
        }, onFailure = {
            Toast.makeText(requireContext(),"Erreur lors de la recherche d'article",Toast.LENGTH_SHORT).show()
        })
    }

}