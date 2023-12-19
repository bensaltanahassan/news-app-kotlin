package com.example.newsapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Newss(
    var  titleImage:Int,
    var title:String,
    var isFavorite:Boolean = false
    ): Parcelable{

    }
