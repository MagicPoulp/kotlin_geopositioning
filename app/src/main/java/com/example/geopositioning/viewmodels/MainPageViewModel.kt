package com.example.geopositioning.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import com.example.geopositioning.R
import com.example.geopositioning.config.addressRefreshIntervalNano
import com.example.geopositioning.config.minDistanceToUpdateGeoInfo
import com.example.geopositioning.config.positionRefreshInterval
import com.example.geopositioning.config.trialMode
import com.example.geopositioning.models.GeokeoAddressComponents
import com.example.geopositioning.models.Position
import com.example.geopositioning.repositories.GeokeoRepository
import com.example.geopositioning.repositories.PositioningRepository
import com.example.geopositioning.utilities.LocationFunctions.Companion.distance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val positioningRepository: PositioningRepository,
    private val geokeoRepository: GeokeoRepository
) : ViewModel() {

    // LiveData are observed in the UI
    // the underscore is used to limit the access from outside this file
    private var _position: MutableLiveData<Position> = MutableLiveData()
    val position: LiveData<Position>
        get() = _position
    private var _address: MutableLiveData<String> = MutableLiveData("")
    val address: LiveData<String>
        get() = _address
    private var lastPositionWithGeoInfo = Position(0.0, 0.0)
    private var timeOfLastAddressUpdateAttempt: Long = System.nanoTime()

    fun initialize(activity: Activity) {
        positioningRepository.getLocation(activity)
        geokeoRepository.initialize(activity.resources.getString( R.string.geokeo_apikey))
        setupUiObservers(activity)
    }

    private fun setupUiObservers(activity: Activity) {
        // it is now strongly recommended to use coroutines and FLow instead of RxJava
        // https://stackoverflow.com/questions/42066066/how-kotlin-coroutines-are-better-than-rxkotlin
        (activity as ComponentActivity).lifecycleScope.launch(Dispatchers.Default) {
            while (true) {
                if (positioningRepository.position != Position(0.0, 0.0)
                    && _position.value != positioningRepository.position
                ) {
                    _position.postValue(positioningRepository.position)
                }
                delay(positionRefreshInterval)
            }
        }
        // we update the address every 10s if the distance has moved more than 10 meters
        // we could do it in a totally parallel loop, but it is better to syncrhonize for
        // better responsiveness to changes of location

        // on the main thread to synchronize the value of positioningRepository.position
        // since it is updated on the other thread (see the code above)
        // we measure the elapsed time and if exceeds 10s, we perform the geoInfo update
        position.observe(activity) { newPos ->
            if (newPos == Position(0.0, 0.0)
                || newPos == lastPositionWithGeoInfo) {
                return@observe
            }

            val elapsedTime = System.nanoTime() - timeOfLastAddressUpdateAttempt
            if (elapsedTime < addressRefreshIntervalNano) {
                return@observe
            }
            timeOfLastAddressUpdateAttempt = System.nanoTime()
            activity.lifecycleScope.launch(Dispatchers.Default) {
                // if the distance is too small, we do not fetch geoInfo
                if (!trialMode && distance(newPos, lastPositionWithGeoInfo) < minDistanceToUpdateGeoInfo) {
                    //return@launch
                }
                val geoInfo = geokeoRepository.getGeoInfoForPosition(newPos)
                if (geoInfo.isSuccessful) {
                    geoInfo.body()?.let { it2 ->
                        if (it2.results.isNotEmpty()) {
                            lastPositionWithGeoInfo = newPos
                            _address.postValue(formatAddress(it2.results[0].addressComponents))
                        }
                    }
                }
            }
        }
    }

    private fun formatAddress(data: GeokeoAddressComponents): String {
        val firstPart = data.street ?: data.name
        val secondPart = data.city ?: ""
        val lastPart = data.postcode
        return if (data.city != null) "$firstPart, $secondPart, $lastPart" else "$firstPart, $lastPart"
    }

}
