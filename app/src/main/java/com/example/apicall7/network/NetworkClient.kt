package com.example.apicall7.network

import com.example.apicall7.BASE_URL
import com.example.apicall7.get.PokemonAPInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {



    fun getMyApi() : PokemonAPInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonAPInterface::class.java)
    }
}