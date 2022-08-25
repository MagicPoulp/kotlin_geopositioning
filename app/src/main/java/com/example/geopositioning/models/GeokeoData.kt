package com.example.geopositioning.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeokeoData(
    @JsonProperty("result")
    val result: String,
)
