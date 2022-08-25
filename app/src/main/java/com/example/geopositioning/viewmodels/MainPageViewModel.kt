package com.example.geopositioning.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import com.example.geopositioning.config.addressRefreshInterval
import com.example.geopositioning.config.positionRefreshInterval
import com.example.geopositioning.models.Position
import com.example.geopositioning.repositories.PositioningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val positioningRepository: PositioningRepository
) : ViewModel() {

    // LiveData are observed in the UI
    // the underscore is used to limit the access from outside this file
    private var _position: MutableLiveData<Position> = MutableLiveData()
    val position: LiveData<Position>
        get() = _position

    fun initialize(activity: Activity) {
        positioningRepository.getLocation(activity)
        setupUiObservers(activity)
    }

    private fun setupUiObservers(activity: Activity) {
        // it is now strongly recommended to use coroutines and FLow instead of RxJava
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
        /*
        activity.lifecycleScope.launch(Dispatchers.Default) {
            while (true) {
                if (positioningRepository.position != Position(0.0, 0.0)
                    && _position.value != positioningRepository.position
                ) {
                    _position.value = positioningRepository.position
                }
                delay(addressRefreshInterval)
            }
        }*/
    }

}
