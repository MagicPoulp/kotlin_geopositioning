package com.example.geopositioning.ui

import android.content.pm.PackageManager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.geopositioning.viewmodels.MainPageViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(packageManager: PackageManager,
               mainPageViewModel: MainPageViewModel = getViewModel(),
) {
    Text(text = "Start")
}
