package com.example.place3mapbar


import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext




import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import com.google.maps.android.compose.Polyline





@Composable
fun GoogleMapWithRoute() {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(28.6129, 77.2295), 12f)
    }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // Fetch the route when the screen loads
    LaunchedEffect(Unit) {
        polylinePoints = fetchRoute()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            //properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // Origin Marker
            Marker(
                state = MarkerState(position = LatLng(28.6129, 77.2295)),
                title = "India Gate"
            )

            // Destination Marker
            Marker(
                state = MarkerState(position = LatLng(28.6562, 77.2410)),
                title = "Red Fort"
            )

            // Draw the route
            if (polylinePoints.isNotEmpty()) {
                Polyline(
                    points = polylinePoints,
                    color = Color.Blue,
                    width = 8f
                )
            }
        }
    }
}

