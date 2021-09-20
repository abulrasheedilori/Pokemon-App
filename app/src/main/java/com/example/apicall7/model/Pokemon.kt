package com.example.apicall7.model

data class Pokemon (
        val count: Int,
        val next: Any,
        val previous: Any,
        val results: List<Result>
    )