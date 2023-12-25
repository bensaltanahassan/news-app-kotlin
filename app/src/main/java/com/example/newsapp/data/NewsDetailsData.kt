package com.example.newsapp.data

import Crud
import android.util.Log
import com.example.newsapp.utlis.ResponseGetSingleNewsDetails
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

class NewsDetailsData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"
    private lateinit var articleId:String ;
    private lateinit var token:String ;
    private lateinit var userId:String ;
    constructor(userId:String,articleId:String,token:String){
        this.articleId = articleId
        this.token = token
        this.userId = userId
    }
    fun getSingleNewsDetails(
        onSuccess : (ResponseGetSingleNewsDetails) -> Unit,
        onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/articles/$articleId"
        val json = """
            {
                "userId": "$userId"
            }
        """.trimIndent()
        crud.get(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    Log.d("response",response.toString())
                    val gson = Gson()
                    val getSingleNewsDetailsResponse = gson.fromJson(response, ResponseGetSingleNewsDetails::class.java)
                    onSuccess(getSingleNewsDetailsResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }
}