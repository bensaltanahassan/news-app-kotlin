package com.example.newsapp.utlis

import com.example.newsapp.models.NewsDetails

data class ResponseGetSingleNewsDetails(
    val status: String,
    val data: NewsDetails
)