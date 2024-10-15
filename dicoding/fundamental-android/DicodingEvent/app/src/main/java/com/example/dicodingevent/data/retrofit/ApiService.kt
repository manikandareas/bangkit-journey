package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.DetailEvent
import com.example.dicodingevent.data.response.ListEvents
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getListEvents(
        @Query("active") active: Int,
        @Query("q") query: String,
        @Query("limit") limit: Int = 40
    ): Response<ListEvents>

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): Response<DetailEvent>
}

object EventStatus {
    const val UPCOMING = 1
    const val PAST = 0
    const val ALL = -1
}