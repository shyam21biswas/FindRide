package com.example.place3mapbar



import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


import retrofit2.Call
/*
interface GoogleMapsApiService {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String = "driving",
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>
}

data class DirectionsResponse(
    @SerializedName("routes") val routes: List<Route>
)

data class Route(
    @SerializedName("overview_polyline") val overviewPolyline: OverviewPolyline
)

data class OverviewPolyline(
    @SerializedName("points") val points: String
)

fun String.decodePolyline(): List<LatLng> {
    val poly = mutableListOf<LatLng>()
    var index = 0
    val len = this.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = this[index++].code - 63
            result = result or (b and 0x1F shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = this[index++].code - 63
            result = result or (b and 0x1F shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        poly.add(LatLng(lat / 1E5, lng / 1E5))
    }
    return poly
}


suspend fun fetchRoute(start: LatLng, end: LatLng, apiKey: String): List<LatLng> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(GoogleMapsApiService::class.java)
            val response = service.getDirections(
                origin = "${start.latitude},${start.longitude}",
                destination = "${end.latitude},${end.longitude}",
                apiKey = apiKey
            ).execute()

            if (response.isSuccessful) {
                val route = response.body()?.routes?.firstOrNull()
                if (route != null) {
                    Log.d("RouteDebug", "Route received: ${route.overviewPolyline.points}")
                    return@withContext route.overviewPolyline.points.decodePolyline()
                }
            } else {
                Log.e("RouteError", "API failed: ${response.errorBody()?.string()}")
            }
            emptyList()
        } catch (e: Exception) {
            Log.e("RouteError", "Error: ${e.message}")
            emptyList()
        }
    }
}

@Composable
fun GoogleMapWithRoutes(apiKey: String) {
    val context = LocalContext.current
    val startLocation = LatLng(28.6139, 77.2088) // India Gate
    val endLocation = LatLng(28.6563, 77.2321) // Red Fort
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 12f)
    }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // Fetch the route when the screen loads
    LaunchedEffect(Unit) {
        Log.d("RouteDebug", "LaunchedEffect triggered!")
        polylinePoints = fetchRoute(startLocation, endLocation, apiKey)
        Log.d("RouteDebug", "Fetched route: $polylinePoints")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            // Markers
            Marker(state = MarkerState(position = startLocation), title = "India Gate")
            Marker(state = MarkerState(position = endLocation), title = "Red Fort")

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
}*/