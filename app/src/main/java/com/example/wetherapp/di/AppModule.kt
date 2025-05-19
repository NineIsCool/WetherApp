package com.example.wetherapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.wetherapp.util.Constants
import com.example.wetherapp.util.LocationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCES_NAME)

val appModule = module {
    single { androidContext().dataStore }
    single { LocationProvider(get()) }
} 