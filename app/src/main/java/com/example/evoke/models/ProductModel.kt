package com.example.evoke.models

data class ProductModel(
    val id: Int,
    val item: String,
    val title: String,
    val url: String,
    val price: Int,
    val start: Int,
    val image: String
)
