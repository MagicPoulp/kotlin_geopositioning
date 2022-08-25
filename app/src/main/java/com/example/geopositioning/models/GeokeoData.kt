package com.example.geopositioning.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/*
{
  "results": [
    {
      "class": "office",
      "type": "yes",
      "address_components": {
        "name": "Empire State Building",
        "island": "Manhattan Island",
        "street": "5th Avenue",
        "neighbourhood": "Midtown South",
        "subdistrict": "Manhattan",
        "district": "New York County",
        "city": "New York City",
        "state": "New York",
        "postcode": "10018",
        "country": "United States Of America"
      },
      "formatted_address": "Empire State Building,Manhattan Island,5th Avenue,Midtown South,New York County,New York City,10018,United States Of America",
      "geometry": {
        "location": {
          "lat": "40.74843124430164",
          "lng": "-73.9856567114413"
        },
        "viewport": {
          "northeast": {
            "lat": "40.747922600363026",
            "lng": "-73.9864855"
          },
          "southwest": {
            "lat": "40.74894220036315",
            "lng": "-73.98482589999999"
          }
        }
      },
      "osmurl": "https://www.openstreetmap.org/search?query=40.74843124430164%2C-73.9856567114413#map=17/40.74843124430164/-73.9856567114413",
      "distance": "0.0010302984507433km"
    }
  ],
  "credits": "https://geokeo.com/credits.php",
  "status": "ok"
}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeokeoData(
    @JsonProperty("results")
    val results: Array<GeokeoDataResult>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeokeoData

        if (!results.contentEquals(other.results)) return false

        return true
    }

    override fun hashCode(): Int {
        return results.contentHashCode()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeokeoDataResult(
    @JsonProperty("address_components")
    val addressComponents: GeokeoAddressComponents,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeokeoAddressComponents(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("street")
    val street: String?,
    @JsonProperty("city")
    val city: String?,
    @JsonProperty("postcode")
    val postcode: String,
)
