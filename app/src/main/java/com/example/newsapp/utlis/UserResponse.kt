package com.example.newsapp.utlis

import com.example.newsapp.models.User



data class UploadResponse(
    val status: Boolean = false,
    val message: String = ""
)

data class UpdateUserResponse(
    val status:Boolean,
    val message: String,
    val user: User? = null,
)