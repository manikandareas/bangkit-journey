package com.newsapp.models

import com.google.gson.annotations.SerializedName

data class NewsPortalResponse(
    @SerializedName("maintainer")
    val maintainer: String,
    @SerializedName("github")
    val github: String,
    @SerializedName("endpoints")
    val endpoints: List<NewsPortal>
)
