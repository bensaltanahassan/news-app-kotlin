package com.example.newsapp.models


data class Favoris(
    val _id: String,
    val user: String,
    val article: News,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
