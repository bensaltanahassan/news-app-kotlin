import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class Crud {
    private val client = OkHttpClient()

    interface ResponseCallback {
        fun onResponse(call: Call, response: Response)
        fun onFailure(call: Call, e: IOException)
    }

    fun get(url: String,jsonBody: String,authToken: String?, callback: ResponseCallback) {

        val getBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .get()


        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }


        val getRequest = requestBuilder.build()

        client.newCall(getRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun post(url: String, jsonBody: String,authToken: String?, callback: ResponseCallback) {
        val postBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .post(postBody)

        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val postRequest = requestBuilder.build()


        client.newCall(postRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }
    fun update(url: String, jsonBody: String,authToken: String?, callback: ResponseCallback) {
        val putBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .put(putBody)

        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val putRequest = requestBuilder.build()


        client.newCall(putRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun delete(url: String,jsonBody: String,authToken: String?, callback: ResponseCallback) {
        val deleteBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .delete(deleteBody)

        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val deleteRequest = requestBuilder.build()

        client.newCall(deleteRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun putWithImage(url: String, jsonBody: String,authToken: String?,file:File, callback: ResponseCallback) {
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val jsonRequestBody = RequestBody.create(jsonMediaType, jsonBody)

        val imageMediaType = "image/*".toMediaTypeOrNull()
        val imageRequestBody = file.asRequestBody(imageMediaType)

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("data", null, jsonRequestBody)
            .addFormDataPart("file", file.name, imageRequestBody)
            .build()

        val requestBuilder  = Request.Builder()

        if (authToken != null) {
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val request = requestBuilder
            .url(url)
            .put(multipartBody)
            .build()




        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }


}
