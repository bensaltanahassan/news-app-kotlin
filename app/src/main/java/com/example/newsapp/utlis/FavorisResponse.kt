package com.example.newsapp.utlis

import com.example.newsapp.models.Category
import com.example.newsapp.models.Favoris
import com.example.newsapp.models.News


data class ResponseDeleteFavoris(
    val status: String,
    val data: String
)

data class ResponseAddFavoris(
    val status: String,
    val message:String ="",
    val data: Favoris? = null
)


data class ResponseGetFavoris(
    val status: String,
    val data: List<Favoris>
)
