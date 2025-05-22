package com.example.wetherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wetherapp.presentation.screens.ForecastScreen
import com.example.wetherapp.presentation.screens.WeatherScreen
import com.example.wetherapp.presentation.viewModel.WeatherViewModel
import com.example.wetherapp.presentation.screens.MapScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    onRequestLocationPermission: () -> Unit,
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(route = Screen.Weather.route) {
            WeatherScreen(
                viewModel = weatherViewModel,
                onNavigateToForecast = {
                    navController.navigate(Screen.Forecast.route)
                },
                onNavigateToMap = {
                    navController.navigate(Screen.Map.route)
                },
                onRequestLocationPermission = onRequestLocationPermission
            )
        }
        
        composable(route = Screen.Forecast.route) {
            ForecastScreen(
                weatherViewModel = weatherViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Map.route) {
            MapScreen(
                onLocationSelected = { point ->
                    weatherViewModel.fetchWeatherByMapPoint(point)
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Weather : Screen("weather_screen")
    object Forecast : Screen("forecast_screen")
    object Map : Screen("map_screen")
} 