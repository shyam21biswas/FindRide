package com.example.place3mapbar

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest


import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.items

import com.google.android.libraries.places.api.model.Place



@Composable
fun AutoCompleteSearchBar(
    modifier: Modifier = Modifier,
    context: Context,
    textname : String = "",
    isPickup: Boolean, // To determine whether it's pickup or destination
    onLocationSelected: (String, LatLng) -> Unit,

) {
    val placesClient = Places.createClient(context)
    var query by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf(emptyList<AutocompletePrediction>()) }

    Column(modifier = modifier.background(Color.White, shape = RoundedCornerShape(18.dp))) {
        OutlinedTextField(
            shape = RoundedCornerShape(18.dp),
            value = query,
            onValueChange = { newValue ->
                query = newValue
                if (newValue.length > 3) {
                    val request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(newValue)
                        .build()

                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->
                            predictions = response.autocompletePredictions
                        }
                        .addOnFailureListener {
                            predictions = emptyList()
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = textname) },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { query = ""; predictions = emptyList() }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        )


        // Show Predictions as a Dropdown List
        LazyColumn(modifier = Modifier.background(Color.White)) {
            items(predictions) { prediction ->
                Text(
                    text = prediction.getFullText(null).toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            query = prediction.getFullText(null).toString()
                            val request = FetchPlaceRequest.builder(prediction.placeId, listOf(Place.Field.LAT_LNG)).build()
                            placesClient.fetchPlace(request)
                                //main line when sccuss.....................
                                .addOnSuccessListener { response ->
                                    response.place.latLng?.let { latLng ->
                                        if (isPickup) {
                                            pickupLocation.value = latLng
                                        } else {
                                            destinationLocation.value = latLng
                                        }
                                        onLocationSelected(prediction.placeId, latLng)
                                    }
                                }
                            predictions = emptyList()
                        }
                        .padding(12.dp)
                )
            }
        }
    }
}
