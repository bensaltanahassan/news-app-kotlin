package com.example.newsapp.data

import Crud
import android.util.Log
import com.example.newsapp.utlis.ResponseHomeData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class HomeData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"
    private val userId:String = "657b33fadc7d9d7b39d28b56"
    private val token:String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1Nzc1OGJhZTZmMTU3OTI4NTE1ZmFkNCIsImlzQWRtaW4iOmZhbHNlLCJpYXQiOjE3MDI0MjE5NzV9.BhYCIUg0BZu5ftQ37jOBM2Br953aWexonukt6vmc4N0"

    fun getHomeData(
         onSuccess : (ResponseHomeData) -> Unit,
            onFailure : (String) -> Unit
    ) {
        val homeUrl : String = "$baseUrl/home"
        val userId:String = "$userId"
        val json = """
            {
                "userId": "$userId"
            }
        """.trimIndent()
        val token:String = token
        crud.post(homeUrl,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val homeResponse = gson.fromJson(response,ResponseHomeData::class.java)
                    onSuccess(homeResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }


        )
    }
}