package com.example.newsapp.utlis

import com.example.newsapp.models.News
import com.example.newsapp.models.Rating

data class ResponseNewsData(
    val status: String,
    val data: List<News>
)


data class GetSingleNewsResponse(
    val status: String,
    val data: SingleNewsResponse
)


data class  SingleNewsResponse(
    val article: News,
    val rating: Rating? = null,
    val avgRating: Int? = null,
)