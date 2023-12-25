package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsDetails(val article: News,
                       var rating : Rate?,
    var avgRating : Double):Parcelable

