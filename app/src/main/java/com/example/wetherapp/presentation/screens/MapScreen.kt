package com.example.wetherapp.presentation.screens

import android.content.Context
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.wetherapp.util.LocationProvider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class MarkerData(
    val marker: PlacemarkMapObject,
    val tapListener: MapObjectTapListener?
)

class MapMarkerManager(
    private val context: Context,
    private val mapObjectCollection: MapObjectCollection
) {
    private val markers = mutableListOf<MarkerData>()

    fun addMarker(
        latitude: Double,
        longitude: Double,
        imageRes: Int,
        userData: Any? = null,
        tapListener: MapObjectTapListener? = null
    ): PlacemarkMapObject {
        val marker = mapObjectCollection.addPlacemark(
            Point(latitude, longitude),
            ImageProvider.fromResource(context, imageRes)
        )
        marker.setUserData(userData)
        
        tapListener?.let {
            marker.addTapListener(it)
        }
        
        markers.add(MarkerData(marker, tapListener))
        return marker
    }

    fun clearMarkers() {
        markers.forEach { markerData ->
            markerData.tapListener?.let { listener ->
                markerData.marker.removeTapListener(listener)
            }
            markerData.marker.parent.remove(markerData.marker)
        }
        markers.clear()
    }
}

class MapListeners(
    private val onLocationSelected: (Point) -> Unit
) {
    val inputListener = object : InputListener {
        override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
            onLocationSelected(point)
        }

        override fun onMapLongTap(map: com.yandex.mapkit.map.Map, point: Point) {
        }
    }
}

@Composable
fun MapScreen(
    onLocationSelected: (Point) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var markerManager by remember { mutableStateOf<MapMarkerManager?>(null) }
    val locationProvider = remember { LocationProvider(context) }
    val mapListeners = remember { MapListeners(onLocationSelected) }
    
    LaunchedEffect(Unit) {
        if (locationProvider.hasLocationPermission()) {
            CoroutineScope(Dispatchers.Main).launch {
                val location = locationProvider.getCurrentLocation()
                location?.let { loc ->
                    mapView?.mapWindow?.map?.move(
                        CameraPosition(Point(loc.latitude, loc.longitude), 10.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 5f),
                        null
                    )
                }
            }
        }
    }
    
    DisposableEffect(lifecycleOwner) {
        mapView = MapView(context).apply {
            mapWindow.map.move(
                CameraPosition(Point(55.751244, 37.618423), 16.5f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 5f),
                null
            )
            
            markerManager = MapMarkerManager(context, mapWindow.map.mapObjects.addCollection())
            mapWindow.map.addInputListener(mapListeners.inputListener)
        }

        onDispose {
            markerManager?.clearMarkers()
            mapView?.mapWindow?.map?.removeInputListener(mapListeners.inputListener)
            mapView?.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView?.onStart()
                }
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                    mapView?.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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