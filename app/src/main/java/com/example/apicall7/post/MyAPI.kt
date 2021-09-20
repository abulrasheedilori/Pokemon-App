package com.example.apicall7.post

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

//an interface which houses our uploadImage function to post our image
interface MyAPI {
    @Multipart
    @POST("api/v1/upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    //an object operator to invoke retrofit to post data to our api
    companion object {
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                .baseUrl("https://darot-image-upload-service.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyAPI::class.java)
        }
    }
}