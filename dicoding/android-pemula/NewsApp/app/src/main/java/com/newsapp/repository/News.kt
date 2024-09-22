package com.newsapp.repository

import com.newsapp.models.NewsCategoryResponse
import com.newsapp.models.NewsPortalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsRepository {
    @GET(".")
    suspend fun getAllNewsPortal(): Response<NewsPortalResponse>

    @GET("{portalNews}/{categoryNews}")
    suspend fun getByCategoryNews(
        @Path("portalNews") portalNews: String,
        @Path("categoryNews") categoryNews: String
    ): Response<NewsCategoryResponse>
}

