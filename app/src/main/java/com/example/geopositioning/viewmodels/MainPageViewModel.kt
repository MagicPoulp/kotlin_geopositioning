package com.example.geopositioning.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import com.example.geopositioning.config.refreshInterval
import com.example.geopositioning.models.Position
import com.example.geopositioning.repositories.PositioningRepository
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
        (activity as ComponentActivity).lifecycleScope.launch {
            while (true) {
                if (positioningRepository.position != Position(0.0, 0.0)
                    && _position.value != positioningRepository.position
                ) {
                    _position.value = positioningRepository.position
                }
                delay(refreshInterval)
            }
        }
    }

}
