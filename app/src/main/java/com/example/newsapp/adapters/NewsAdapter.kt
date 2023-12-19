package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.HomeFragmentDirections
import com.example.newsapp.R
import com.example.newsapp.models.Newss
import com.google.android.material.imageview.ShapeableImageView


class NewsAdapter (
    private val newssList: ArrayList<Newss>,
    private val navController: NavController

    ): RecyclerView.Adapter<NewsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_new,parent,false);
        val holder = MyViewHolder(itemView)


        return  holder;

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newssList[position]
        holder.image.setImageResource(currentItem.titleImage);
        holder.title.text= currentItem.title
        val bookmarkButton = holder.bookmarkButtonNew
        val currentNew = holder.currentNew
        currentNew.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToArticleDetailsFragment(newssList[position])
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
            newssList[index].isFavorite = false
        }else{
            bookMarkButton.setImageDrawable(ContextCompat.getDrawable(bookMarkButton.context,R.drawable.baseline_bookmark_24))
            newssList[index].isFavorite = true
        }
    }

    private fun isFavorite(index:Int):Boolean{
        return newssList[index].isFavorite
    }

    override fun getItemCount(): Int {
        return newssList.size;
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val currentNew : LinearLayout = itemView.findViewById(R.id.newId)
        val image : ShapeableImageView  = itemView.findViewById(R.id.imageViewNew)
        val title : TextView  = itemView.findViewById(R.id.titleTextViewNew)
        val bookmarkButtonNew : ImageButton = itemView.findViewById(R.id.bookmarkButtonNew)
    }


}