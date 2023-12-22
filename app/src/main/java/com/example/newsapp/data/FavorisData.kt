package com.example.newsapp.data

import Crud
import com.example.newsapp.models.Favoris
import com.example.newsapp.models.News
import com.example.newsapp.utlis.ResponseAddFavoris
import com.example.newsapp.utlis.ResponseDeleteFavoris
import com.example.newsapp.utlis.ResponseGetFavoris
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class FavorisData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"
    private val userId:String = "657b33fadc7d9d7b39d28b56"
    private val token:String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1Nzc1OGJhZTZmMTU3OTI4NTE1ZmFkNCIsImlzQWRtaW4iOmZhbHNlLCJpYXQiOjE3MDI0MjE5NzV9.BhYCIUg0BZu5ftQ37jOBM2Br953aWexonukt6vmc4N0"

    fun addToFavoris(news: News,
                             onSuccess : (ResponseAddFavoris) -> Unit,
                             onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/favoris"
        val userId:String = userId
        val json = """
            {
                "userId": "$userId",
                "articleId": "${news._id}"
            }
        """.trimIndent()
        val token:String = token
        crud.post(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val addToFavorisResponse = gson.fromJson(response, ResponseAddFavoris::class.java)
                    onSuccess(addToFavorisResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


    fun deleteFromFavoris(favoris: Favoris,
                     onSuccess : (ResponseDeleteFavoris) -> Unit,
                     onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/favoris"
        val json = """
            {
                "favorisId": "${favoris._id}"
            }
        """.trimIndent()
        val token:String = token
        crud.delete(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val deleteFromFavorisResponse = gson.fromJson(response, ResponseDeleteFavoris::class.java)
                    onSuccess(deleteFromFavorisResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }



    private fun getAllFavoris(userId: String,
                             onSuccess : (ResponseGetFavoris) -> Unit,
                             onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/favoris"
        val userId:String = userId
        val json = """
            {
                "userId": "$userId",
            }
        """.trimIndent()
        val token:String = token
        crud.post(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val getFavorisResponse = gson.fromJson(response, ResponseGetFavoris::class.java)
                    onSuccess(getFavorisResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


}