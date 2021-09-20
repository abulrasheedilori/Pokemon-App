package com.example.apicall7.get

object SplitNameAddress {
        fun splitName(pokemonsNameForDetails:String):String{
            return "https://pokeapi.co/api/v2/pokemon/${pokemonsNameForDetails}"
        }
}