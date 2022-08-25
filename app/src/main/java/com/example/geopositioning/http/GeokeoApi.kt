package com.example.geopositioning.http

// Retrofit interface

import com.example.geopositioning.models.GeokeoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// https://geokeo.com/geocode/v1/reverse.php?lat=40.7484283080311&lng=-73.9856483067424&api=9da1fbafc8827213e41262a09afc3427
interface GeokeoApi {
    @GET("/geocode/v1/reverse.php")
    suspend fun geocodeInverse(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("api") api: String
    ): Response<GeokeoData>
}
