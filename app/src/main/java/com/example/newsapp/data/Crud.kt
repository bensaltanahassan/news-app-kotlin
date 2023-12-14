import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Crud {
    private val client = OkHttpClient()

    interface ResponseCallback {
        fun onResponse(call: Call, response: Response)
        fun onFailure(call: Call, e: IOException)
    }

    fun get(url: String, callback: ResponseCallback) {
        val request = Request.Builder()
            .url(url)
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

    fun post(url: String, jsonBody: String, callback: ResponseCallback) {
        val postBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val postRequest = Request.Builder()
            .url(url)
            .post(postBody)
            .build()

        client.newCall(postRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }
    fun update(url: String, jsonBody: String, callback: ResponseCallback) {
        val putBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val putRequest = Request.Builder()
            .url(url)
            .put(putBody)
            .build()

        client.newCall(putRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun delete(url: String, callback: ResponseCallback) {
        val request = Request.Builder()
            .url(url)
            .delete()
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
