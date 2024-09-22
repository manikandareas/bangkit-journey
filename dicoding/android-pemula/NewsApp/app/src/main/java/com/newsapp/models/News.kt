package com.newsapp.models

import com.google.gson.annotations.SerializedName

data class News(

	@SerializedName("thumbnail")
	val thumbnail: String? = null,

	@SerializedName("link")
	val link: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("pubDate")
	val pubDate: String? = null
)
