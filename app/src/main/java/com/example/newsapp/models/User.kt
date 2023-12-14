package com.example.newsapp.models

data class User(val firstName: String , val lastName: String,val email: String,val phone : String , val password : String ,
                val isAdmin : Boolean,
                val isAccountVerified : Boolean,
                val profilePhoto : Image)
