package com.example.wetherapp.di

import com.example.wetherapp.presentation.forecast.ForecastViewModel
import com.example.wetherapp.presentation.search.SearchViewModel
import com.example.wetherapp.presentation.weather.WeatherViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { WeatherViewModel(get()) }
    viewModel { ForecastViewModel(get()) }
    viewModel { SearchViewModel(get()) }
} 