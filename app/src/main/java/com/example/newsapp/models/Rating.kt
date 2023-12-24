package com.example.newsapp.models

data class Rating(
    val _id: String,
    var user: User,
    var article:News,
    var rating: Int,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
