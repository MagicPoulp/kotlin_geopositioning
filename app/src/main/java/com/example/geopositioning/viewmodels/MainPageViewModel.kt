package com.example.geopositioning.viewmodels

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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Long.max
import kotlin.system.measureTimeMillis

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

    // just a small example using RxJava
    private val disposable = CompositeDisposable()
    private val observableExample: PublishSubject<Position> = PublishSubject.create()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun initialize(activity: Activity) {
        disposable.add(observableExample.subscribe {
            println("New location. Lat: " + it.latitude + ", Lng: " + it.longitude)
        })
        positioningRepository.getLocation(activity)
        geokeoRepository.initialize(activity.resources.getString( R.string.geokeo_apikey))
        setupUiObservers(activity)
    }

    private fun setupUiObservers(activity: Activity) {
        // -----> Why RxJava was not use and LiveData and coroutines were used instead
        // it is now strongly recommended to use coroutines and Flow instead of RxJava
        // https://stackoverflow.com/questions/42066066/how-kotlin-coroutines-are-better-than-rxkotlin
        // Moreover, we can observer on livedata in association with a lifecycle owner (see .observe())
        // LiveData is bound directly to the UI using the expression "by ... observeAsState".
        // So cannot force us to add extra RxJava observers.
        (activity as ComponentActivity).lifecycleScope.launch(Dispatchers.Default) { // in a Dispatchers.Default coroutine
            while (true) {
                val executionTime = measureTimeMillis {
                    if (positioningRepository.position != Position(0.0, 0.0)
                        && _position.value != positioningRepository.position
                    ) {
                        _position.postValue(positioningRepository.position)
                        observableExample.onNext(positioningRepository.position)
                    }
                }
                // every second (and we adjust with the execution time from the code)
                delay(max(0L, positionRefreshInterval - executionTime))
            }
        }
        // we update the address every 10s if the distance has moved more than 10 meters
        // we could do it in a totally parallel loop, but it is better to synchronize for
        // better responsiveness to changes of location

        // on the main thread to synchronize the value of positioningRepository.position
        // since it is updated on the other thread (see the code above)
        // we measure the elapsed time and if exceeds 10s, we perform the geoInfo update
        position.observe(activity) { newPos -> // on the Main thread
            if (newPos == Position(0.0, 0.0)
                || newPos == lastPositionWithGeoInfo) {
                return@observe
            }

            val elapsedTime = System.nanoTime() - timeOfLastAddressUpdateAttempt
            if (elapsedTime < addressRefreshIntervalNano) { // every 10s we move forward
                return@observe
            }
            timeOfLastAddressUpdateAttempt = System.nanoTime()
            activity.lifecycleScope.launch(Dispatchers.IO) { // in a Dispatchers.IO coroutine since we use the network
                // if the distance is too small, we do not fetch geoInfo
                if (!trialMode && distance(newPos, lastPositionWithGeoInfo) < minDistanceToUpdateGeoInfo) {
                    return@launch
                }
                try {
                    val geoInfo = geokeoRepository.getGeoInfoForPosition(newPos)
                    if (geoInfo.isSuccessful) {
                        geoInfo.body()?.let { it2 ->
                            if (it2.results.isNotEmpty()) {
                                lastPositionWithGeoInfo = newPos
                                _address.postValue(formatAddress(it2.results[0].addressComponents))
                            }
                        }
                    }
                } catch (e: Exception) {
                    println(e.message)
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
