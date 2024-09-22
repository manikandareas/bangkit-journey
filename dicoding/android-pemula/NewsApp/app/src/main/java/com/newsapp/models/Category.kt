package com.newsapp.models
import com.google.gson.annotations.SerializedName
data class Category(
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String
)
