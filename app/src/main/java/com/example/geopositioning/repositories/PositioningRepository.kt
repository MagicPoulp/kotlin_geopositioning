package com.example.geopositioning.repositories

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geopositioning.config.locationPermissionCode
import com.example.geopositioning.models.Position
import org.koin.core.component.KoinComponent

/*
source
https://medium.com/@hasperong/get-current-location-with-latitude-and-longtitude-using-kotlin-2ef6c94c7b76
 */
class PositioningRepository: KoinComponent, LocationListener {
    lateinit var locationManager: LocationManager
    var position = Position(0.0, 0.0)

    fun getLocation(activity: Activity) {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        // Temporary to debug
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)
    }

    override fun onLocationChanged(location: Location) {
        val newPos = Position(location.latitude, location.longitude)
        position = newPos
        println("Latitude: " + location.latitude + " , Longitude: " + location.longitude)
    }

}
