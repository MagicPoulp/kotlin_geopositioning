package com.example.geopositioning.repositories

import com.example.geopositioning.config.geokeoBaseUrl
import com.example.geopositioning.http.GeokeoApi
import com.example.geopositioning.http.RetrofitHelper
import com.example.geopositioning.models.GeokeoData
import com.example.geopositioning.models.Position
import retrofit2.Response

class GeokeoRepository {

    private lateinit var apiKey: String

    fun initialize(apiKey: String) {
        this.apiKey = apiKey
    }

    private val retrofitHelper = RetrofitHelper.getInstance(baseUrl = geokeoBaseUrl)
        .create(GeokeoApi::class.java)

    suspend fun getGeoInfoForPosition(position: Position): Response<GeokeoData> {
        return retrofitHelper.geocodeInverse(lat = position.latitude.toString(), lng = position.longitude.toString(), api = apiKey)
    }
}