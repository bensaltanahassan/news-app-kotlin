package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    var  image:Image,
                var title:String,
                var author:String,
                var content:String,
                var categoryId:String,
                var isFavorite:Boolean = false,
): Parcelable
