package com.example.place3mapbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class RideViewModel : ViewModel() {
    var estimatedPrice by mutableStateOf<Double?>(null)
        private set

    fun calculateEstimate(pickup: LatLng, drop: LatLng) {
        // Your API logic here — once fetched, update:
        estimatedPrice = fetchUberEstimate(pickup, drop)
    }

    private fun fetchUberEstimate(pickup: LatLng, drop: LatLng): Double {
        // Dummy logic — replace with real API
        return (pickup.latitude + drop.latitude) * 2.5
    }
}
