package com.example.wetherapp.di

import com.example.wetherapp.presentation.viewModel.ForecastViewModel
import com.example.wetherapp.presentation.viewModel.SearchViewModel
import com.example.wetherapp.presentation.viewModel.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { WeatherViewModel(get()) }
    viewModel { ForecastViewModel(get()) }
    viewModel { SearchViewModel(get()) }
} 