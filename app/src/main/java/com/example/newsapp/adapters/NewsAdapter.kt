package com.example.newsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.HomeFragmentDirections
import com.example.newsapp.R
import com.example.newsapp.models.News


class NewsAdapter (
    private val newsList: ArrayList<News>,
    private val navController: NavController

    ): RecyclerView.Adapter<NewsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_new,parent,false);
        val holder = MyViewHolder(itemView)
        return holder;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.image.visibility = View.GONE
        holder.progressBar.visibility = View.VISIBLE
        //load the images
        val request = coil.request.ImageRequest.Builder(holder.image.context)
            .data(currentItem.image.url)
            .target(holder.image)
            .target(
                onStart = {
                    holder.progressBar.visibility = View.VISIBLE
                },
                onSuccess = { result ->
                    holder.progressBar.visibility = View.GONE
                    holder.image.visibility = View.VISIBLE
                    holder.image.setImageDrawable(result)
                },
                onError = { _ ->
                    Log.d("Error", "Error loading image")
                }
            ).build()
        coil.ImageLoader(holder.image.context).enqueue(request)


        holder.title.text= currentItem.title
        holder.date.text = "April 12, 2021"
        holder.author.text = currentItem.author

        val bookmarkButton = holder.bookmarkButtonNew
        val currentNew = holder.currentNew
        currentNew.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToArticleDetailsFragment(newsList[position])
            navController.navigate(action)
        }

        if(isFavorite(position)){
            bookmarkButton.setImageDrawable(ContextCompat.getDrawable(bookmarkButton.context,R.drawable.baseline_bookmark_24))
        }else{
            bookmarkButton.setImageDrawable(ContextCompat.getDrawable(bookmarkButton.context,R.drawable.baseline_bookmark_border_24))
        }
        bookmarkButton.setOnClickListener {
            onClickMarkButton(bookmarkButton,position)
        }
    }


    private fun onClickMarkButton(bookMarkButton: ImageButton, index:Int){
        if(isFavorite(index)){
            bookMarkButton.setImageDrawable(ContextCompat.getDrawable(bookMarkButton.context,R.drawable.baseline_bookmark_border_24))
            newsList[index].isFavorite = false
        }else{
            bookMarkButton.setImageDrawable(ContextCompat.getDrawable(bookMarkButton.context,R.drawable.baseline_bookmark_24))
            newsList[index].isFavorite = true
        }
    }

    private fun isFavorite(index:Int):Boolean{
        return newsList[index].isFavorite
    }

    override fun getItemCount(): Int {
        return newsList.size;
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val progressBar :ProgressBar = itemView.findViewById(R.id.progressBarNew)
        val currentNew : LinearLayout = itemView.findViewById(R.id.newId)
        val image : ImageView  = currentNew.findViewById(R.id.imageViewNew)
        val title : TextView  = currentNew.findViewById(R.id.titleTextViewNew)
        val date : TextView = currentNew.findViewById(R.id.timeTextViewNew)
        val author: TextView = currentNew.findViewById(R.id.authorTextViewNew)
        val bookmarkButtonNew : ImageButton = currentNew.findViewById(R.id.bookmarkButtonNew)
    }


}