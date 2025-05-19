package com.example.wetherapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wetherapp.util.LocationProvider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    onLocationSelected: (Point) -> Unit
) {
    val context = LocalContext.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val locationProvider = remember { LocationProvider(context) }
    
    LaunchedEffect(Unit) {
        if (locationProvider.hasLocationPermission()) {
            CoroutineScope(Dispatchers.Main).launch {
                val location = locationProvider.getCurrentLocation()
                location?.let { loc ->
                    mapView?.mapWindow?.map?.move(
                        CameraPosition(Point(loc.latitude, loc.longitude), 16.5f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 5f),
                        null
                    )
                }
            }
        }
    }
    
    DisposableEffect(Unit) {
        mapView = MapView(context).apply {
            mapWindow.map.move(
                CameraPosition(Point(55.751244, 37.618423), 16.5f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 5f),
                null
            )
            
            mapWindow.map.addInputListener(object : InputListener {
                override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
                    onLocationSelected(point)
                }

                override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
                }
            })
        }
        
        onDispose {
            mapView?.onStop()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        mapView?.let { map ->
            AndroidView(
                factory = { map },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
} 