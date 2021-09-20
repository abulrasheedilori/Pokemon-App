package com.example.apicall7.get

    //create object class to manupulate the image URL
    object SplitImageAddress {
        fun splitFunction(pokemonName:String):String{
            return "https://img.pokemondb.net/artwork/large/${pokemonName}.jpg"
        }

    }