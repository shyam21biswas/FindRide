package com.example.place3mapbar

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import timber.log.Timber
import androidx.compose.runtime.key
import androidx.navigation.NavController
import kotlinx.coroutines.delay

//to store global user current location coordinates
var ved: String = ""
var shyam : String = ""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(mapViewModel: MapViewModel, navController: NavController) {
    // Initialize the camera position state, which controls the camera's position on the map
    val cameraPositionState = rememberCameraPositionState()
    var selectedLocations by remember { mutableStateOf<LatLng?>(null) }


    //for upadate the pickup location and destination location
    var pickup by remember {mutableStateOf(pickupLocation.value) }
    var destination by remember { mutableStateOf(destinationLocation.value) }


    /*val cameraPositionState2 = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(26.8467, 80.9462), 12f)
    }*/
    // Variables to store the coordinates
    val currentCoordinates = remember { mutableStateOf(LatLng(26.8467, 80.9462)) }

    // Obtain the current context
    val context = LocalContext.current
    // Observe the user's location from the ViewModel
    val userLocation by mapViewModel.userLocation
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    // Load the raw resource as a string
    val styleJson = remember {
        context.resources.openRawResource(R.raw.stylemap)
            .bufferedReader().use { it.readText() }
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = MapStyleOptions(styleJson)
            )
        )
    }
    // Observe the selected location from the ViewModel
    val selectedLocation by mapViewModel.selectedLocation

    // Handle permission requests for accessing fine location
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Fetch the user's location and update the camera if permission is granted
            mapViewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            // Handle the case when permission is denied
            Timber.e("Location permission was denied by the user.")
        }
    }


// Request the location permission when the composable is launched

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            // Check if the location permission is already granted
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Fetch the user's location and update the camera
                mapViewModel.fetchUserLocation(context, fusedLocationClient)
            }

            else -> {
                // Request the location permission if it has not been granted
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    // 🔥 Force recomposition when locations change
    val forceRecompositionKey = remember(pickup, destination) { Any() }

    // Layout that includes the search bar and the map, arranged in a vertical column

     Box(modifier = Modifier.fillMaxSize())

    {

        key(forceRecompositionKey) {
            if (showroute == true && pickup != null && destination != null) {
                GoogleMapWithRoutes(
                    apiKey = "AIzaSyBmOX8MxQo37oCgKuO1lMF0saxMoUx6GKU", pickup, destination
                )
            } else {


                // Display the Google Map
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    //properties = mapProperties,
                    properties = mapProperties,

                    onMapClick = { latLng ->
                        // Update the coordinates when the map is clicked
                        currentCoordinates.value = latLng
                    }
                ) {

                    userLocation?.let {
                        Marker(
                            state = MarkerState(position = it), // Place the marker at the user's location
                            title = "Your Location", // Set the title for the marker
                            snippet = "This is where you are currently located."
                            // Set the snippet for the marker
                        )
                        ved = it.toString()

                        // Store the user's location as a string

                        // Move the camera to the user's location with a zoom level of 10f
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 18f)
                    }
                    // If a location was selected from the search bar, place a marker there
                    selectedLocation?.let {
                        Marker(
                            state = MarkerState(position = it), // Place the marker at the selected location
                            title = "Selected Location", // Set the title for the marker
                            snippet = "This is the place you selected." // Set the snippet for the marker
                        )
                        // Move the camera to the selected location with a zoom level of 15f
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 8f)
                    }

                    // Marker for Lucknow
//                    Marker(
//                        state = rememberMarkerState(
//                            position = LatLng(
//                                26.8467,
//                                80.9462
//                            )
//                        ), // Lucknow coordinates
//                        title = "Lucknow",
//                        snippet = "VEDANSHI HOME"
//                    )




                }

            }
        }

        // Search Bar
        AutoCompleteSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            context = context , textname = "search your location", isPickup = true
        ) { placeId, latLng ->
            selectedLocations = latLng

            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            )




        }



        var showBottomSheet by remember { mutableStateOf(false) }

         FloatingActionButton(
             // the click button to open the bottom sheet...............
        onClick = { showBottomSheet = true },
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 60.dp),
        containerColor = Color.White
    ) {
        Text("  Destination  ", color = Color.Black, fontWeight = FontWeight.Medium , fontSize = 30.sp)
    }
        //  Half-Screen Bottom Sheet   EDIT ON YOUR OWN
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                tonalElevation = 15.dp , shape = MaterialTheme.shapes.extraLarge,
                containerColor = Color.Transparent

                ) {



                //  Bottom Sheet Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Set Your Routes", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    // Search Bar
                    AutoCompleteSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        context = context , textname = "Pick up location ..." , isPickup = true
                    ) { placeId, latLng ->
                        selectedLocations = latLng
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        )

                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Search Bar for destination ..................................

                    AutoCompleteSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            ,
                        context = context , textname =  "Destination drop location... " , isPickup = false
                    ) { placeId, latLng ->
                        selectedLocations = latLng
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        showBottomSheet = false



                        navController.navigate("home")


                        showroute = true
                                     } ,
                        ) {
                        Text("Find your Routes")
                    }





                }
            }
        }


    }
}


