package com.newsapp.models

import com.google.gson.annotations.SerializedName

data class NewsCategoryResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: Data
)


data class Data(
    @SerializedName("link")
    val link: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("posts")
    val posts: List<News>
)