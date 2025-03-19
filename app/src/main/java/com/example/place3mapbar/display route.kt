package com.example.place3mapbar
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.net.URL


suspend fun fetchRoute(): List<LatLng> {
    val origin = "28.6129,77.2295"  // India Gate
    val destination = "28.6562,77.2410"  // Red Fort
    val apiKey = "AIzaSyBbW_UEhUa4siydX7A1JWc-NFmRqVgTYVo"

    val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&key=$apiKey"

    return withContext(Dispatchers.IO) {
        try {
            val response = URL(url).readText()
            val jsonObject = JSONObject(response)
            val routes = jsonObject.getJSONArray("routes")

            if (routes.length() > 0) {
                val overviewPolyline = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")

                return@withContext decodePolyline(overviewPolyline)
            } else {
                return@withContext emptyList()
            }
        } catch (e: Exception) {
            Timber.e("Error fetching route: ${e.localizedMessage}")
            return@withContext emptyList()
        }
    }
}


fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1F shl shift)
            shift += 5
        } while (b >= 0x20)
        val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dLat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1F shl shift)
            shift += 5
        } while (b >= 0x20)
        val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dLng

        poly.add(LatLng(lat / 1E5, lng / 1E5))
    }
    return poly
}


