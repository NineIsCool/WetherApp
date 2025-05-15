package com.example.wetherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wetherapp.presentation.forecast.ForecastScreen
import com.example.wetherapp.presentation.weather.WeatherScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    onRequestLocationPermission: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(route = Screen.Weather.route) {
            WeatherScreen(
                onNavigateToForecast = {
                    navController.navigate(Screen.Forecast.route)
                },
                onRequestLocationPermission = onRequestLocationPermission
            )
        }
        
        composable(route = Screen.Forecast.route) {
            ForecastScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Weather : Screen("weather_screen")
    object Forecast : Screen("forecast_screen")
} 