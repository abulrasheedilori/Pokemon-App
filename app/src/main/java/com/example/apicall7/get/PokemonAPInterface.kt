package com.example.apicall7.get

import com.example.apicall7.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//an api interface to make api call and set pararameters to obtain all pokemon on our recyclerview
interface PokemonAPInterface {
    @GET("pokemon")
    fun getProperties(@Query("limit") limit:Int, @Query("offset") offset:Int): Call<Pokemon>


}