package com.example.wetherapp.di

import com.example.wetherapp.data.repository.WeatherRepositoryImpl
import com.example.wetherapp.domain.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get(), get()) }
} 