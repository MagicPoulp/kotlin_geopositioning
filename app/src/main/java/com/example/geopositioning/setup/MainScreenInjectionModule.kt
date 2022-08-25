package com.example.geopositioning.setup


import com.example.geopositioning.viewmodels.MainPageViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val mainScreenInjectionModule = module {
    repositories()
    viewModels()
}

private fun Module.repositories() {
//    factory { PackageManagerRepository() }
}

fun Module.viewModels() {
    viewModel { MainPageViewModel() }
}
