package com.example.geopositioning.http

// Retrofit interface

import com.example.geopositioning.models.GeokeoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GeokeoApi {
    @GET("/")
    suspend fun analyse(@Path("id") idStr: String): Response<GeokeoData>
}
