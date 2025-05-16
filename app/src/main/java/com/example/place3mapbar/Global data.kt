package com.example.place3mapbar

import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng

// Global state for pickup and destination coordinates
var pickupLocation = mutableStateOf<LatLng?>(null)
var destinationLocation = mutableStateOf<LatLng?>(null)
var pickuplog = mutableStateOf<String?>(null)
var destinationlog = mutableStateOf<String?>(null)
var pickuplat = mutableStateOf<String?>(null)
var destinationlat = mutableStateOf<String?>(null)

// Global state for pickup and destination coordinates
//var pickupLocation = mutableStateOf(LatLng(28.6139, 77.2088)) // Default: India Gate
//var destinationLocation = mutableStateOf(LatLng(28.6563, 77.2321)) // Default: Red Fort


var showroute : Boolean = false

var permis = false