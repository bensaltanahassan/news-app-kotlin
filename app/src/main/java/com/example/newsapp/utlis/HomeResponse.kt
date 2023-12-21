package com.example.newsapp.utlis

import com.example.newsapp.models.Category
import com.example.newsapp.models.Favoris
import com.example.newsapp.models.News

data class ResponseHomeData(
    val status: String,
    val data: DataHome
)

data class DataHome(
    val categories: List<Category>,
    val news: List<News>,
    val favoris: List<Favoris>
)