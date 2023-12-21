package com.example.newsapp.utlis

import com.example.newsapp.models.News

data class ResponseNewsData(
    val status: String,
    val data: List<News>
)