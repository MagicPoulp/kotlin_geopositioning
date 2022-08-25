package com.example.geopositioning.utilities

import com.example.geopositioning.models.Position

// https://handyopinion.com/find-distance-between-two-locations-in-kotlin/
class LocationFunctions {
    companion object {
        fun distance(pos1: Position, pos2: Position): Double {
            return 1000 * distanceInKm(pos1.latitude, pos1.longitude, pos2.latitude, pos2.longitude)
        }

        fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = lon1 - lon2
            var dist =
                Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(
                    deg2rad(lat2)
                ) * Math.cos(deg2rad(theta))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.1515
            dist = dist * 1.609344
            return dist
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }
    }
}
