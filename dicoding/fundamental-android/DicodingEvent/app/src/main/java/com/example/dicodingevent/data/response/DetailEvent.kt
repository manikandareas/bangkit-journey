package com.example.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

data class DetailEvent(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("event")
	val event: Event
)
