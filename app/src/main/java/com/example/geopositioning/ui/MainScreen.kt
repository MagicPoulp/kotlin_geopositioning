package com.example.geopositioning.ui

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.geopositioning.models.Position
import com.example.geopositioning.viewmodels.MainPageViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(packageManager: PackageManager,
               activity: Activity,
               mainPageViewModel: MainPageViewModel = getViewModel(),
) {
    mainPageViewModel.initialize(activity)
    val position2 by mainPageViewModel.position.observeAsState()
    var position = Position(0.0,0.0)
    position2?.let {
        position = it
    }
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        Text(text = "Lat: ${position.latitude}, Lng: ${position.longitude}")
    }
}
