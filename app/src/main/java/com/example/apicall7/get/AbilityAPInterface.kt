package com.example.apicall7.get

import com.apipokemonproject.abilitiesApi.PokemonAbilities
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//an api interface to make api call and set pararameters we wanna obtain from the api
interface AbilityAPInterface {
    @GET("pokemon/{id}")
    fun getData(@Path("id") id:String):
            retrofit2.Call<PokemonAbilities>

    @GET("pokemon/{name}")
    fun getPokemonName(@Query("name") name:String):retrofit2.Call<PokemonAbilities>

    @GET("pokemon/{id}")
    fun getPokemonId(@Query("id") id:Int):retrofit2.Call<PokemonAbilities>
}