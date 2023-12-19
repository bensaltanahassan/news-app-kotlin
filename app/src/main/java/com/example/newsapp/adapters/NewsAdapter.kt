package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.models.New
import com.google.android.material.imageview.ShapeableImageView


class NewsAdapter (private val newsList: ArrayList<New>): RecyclerView.Adapter<NewsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_new,parent,false);
        val holder = MyViewHolder(itemView)


        return  holder;

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.image.setImageResource(currentItem.titleImage);
        holder.title.text= currentItem.title
        val bookmarkButton = holder.bookmarkButtonNew
        bookmarkButton.setOnClickListener {
            bookmarkButton.setColorFilter(
                ContextCompat.getColor(
                    bookmarkButton.context,
                    R.color.red
                )
            )


        }
    }



    override fun getItemCount(): Int {
        return newsList.size;
    }




    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image : ShapeableImageView  = itemView.findViewById(R.id.imageViewNew)
        val title : TextView  = itemView.findViewById(R.id.titleTextViewNew)
        val bookmarkButtonNew : ImageButton = itemView.findViewById(R.id.bookmarkButtonNew)


    }


}