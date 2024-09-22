package com.newsapp.models

import com.google.gson.annotations.SerializedName

data class NewsPortal(
    @SerializedName("name")
    val name: String,
    @SerializedName("paths")
    val paths: List<Category>
)
