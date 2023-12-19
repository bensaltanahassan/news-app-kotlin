package com.example.newsapp.models

data class News(var  image:Image,
                var title:String,
                var author:String,
                var content:String,
                var categoryId:String)
