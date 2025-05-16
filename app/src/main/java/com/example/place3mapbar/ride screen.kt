package com.example.place3mapbar


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import kotlin.random.Random
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.coroutines.delay
import kotlin.math.*


data class Ride(
    val vehicleType: String,
    val distance: Double, // in kilometers
    val trafficCondition: String,
    val price: Double,
    val duration: Int = Random.nextInt(10, 60)
)



@Composable

fun RideApp() {


    var lastPickup by remember { mutableStateOf<LatLng?>(null) }
    var lastDestination by remember { mutableStateOf<LatLng?>(null) }
    var rides by remember { mutableStateOf<List<Ride>>(emptyList()) }

    val currentPickup = pickupLocation.value
    val currentDestination = destinationLocation.value

    LaunchedEffect(currentPickup, currentDestination) {
        if (currentPickup != null && currentDestination != null &&
            (currentPickup != lastPickup || currentDestination != lastDestination)) {

            lastPickup = currentPickup
            lastDestination = currentDestination

            rides = generateAvailableRides(currentPickup, currentDestination)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Available Rides", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        if (rides.isEmpty()) {
            Text("No Ride Available", style = MaterialTheme.typography.headlineSmall)
        } else {
            LazyColumn {
                items(rides) { ride ->
                    RideCard(ride)
                }
            }
        }
    }
}


fun generateAvailableRides(pickup: LatLng?, destination: LatLng?): List<Ride> {
    if (pickup == null || destination == null) return emptyList()

    // Define blocked locations
    val blockedLocations = listOf(
        LatLng(26.2648064, 81.5054372),
        LatLng(26.2720264, 81.5069359)
    )

    // Block if either pickup or destination is within 20 km of any blocked location
    val isNearBlocked = blockedLocations.any { blocked ->
        calculateDistanceInKm(blocked, pickup) <= 20 || calculateDistanceInKm(blocked, destination) <= 20
    }
    if (isNearBlocked) return emptyList()

    val citiesBounds = listOf(
        26.6..27.1 to 80.8..81.1,   // Lucknow
        26.0..26.4 to 81.0..81.5,   // Rae Bareli
        26.2..26.6 to 80.1..80.5,   // Kanpur
        25.2..25.4 to 82.9..83.2    // Varanasi
    )

    fun LatLng.inAnyBounds(): Boolean {
        return citiesBounds.any { bounds -> latitude in bounds.first && longitude in bounds.second }
    }

    val pickupInBounds = pickup.inAnyBounds()
    val destinationInBounds = destination.inAnyBounds()
    val distance = calculateDistanceInKmk(pickup, destination)
    val trafficConditions = listOf("Light", "Moderate", "Heavy")

    val vehicleTypes = if (pickupInBounds && destinationInBounds) {
        listOf("UberX", "UberXL", "UberBlack", "Ola Mini", "Ola Prime", "Ola SUV")
    } else {
        listOf("UberBlack", "Ola SUV")
    }

    return vehicleTypes.map {
        val traffic = trafficConditions.random()
        val price = calculatePrice(it, traffic, distance)
        Ride(vehicleType = it, distance = distance, trafficCondition = traffic, price = price)
    }.sortedBy { it.price }
}


fun calculateDistanceInKmk(start: LatLng, end: LatLng): Double {
    val radius = 6371.0 // Earth's radius in kilometers
    val latDiff = Math.toRadians(end.latitude - start.latitude)
    val lonDiff = Math.toRadians(end.longitude - start.longitude)

    val a = sin(latDiff / 2).pow(2.0) +
            cos(Math.toRadians(start.latitude)) *
            cos(Math.toRadians(end.latitude)) *
            sin(lonDiff / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}



//fun generateAvailableRides(pickup: LatLng?, destination: LatLng?): List<Ride> {
//    if (pickup == null || destination == null) return emptyList()
//
//    val citiesBounds = listOf(
//        26.6..27.1 to 80.8..81.1,   // Lucknow
//        26.0..26.4 to 81.0..81.5,   // Rae Bareli
//        26.2..26.6 to 80.1..80.5,   // Kanpur
//        25.2..25.4 to 82.9..83.2    // Varanasi
//    )
//
//    fun LatLng.inAnyBounds(): Boolean {
//        return citiesBounds.any { bounds -> latitude in bounds.first && longitude in bounds.second }
//    }
//
//    val pickupInBounds = pickup.inAnyBounds()
//    val destinationInBounds = destination.inAnyBounds()
//    val distance = calculateDistanceInKm(pickup, destination)
//    val trafficConditions = listOf("Light", "Moderate", "Heavy")
//
//    val vehicleTypes = if (pickupInBounds && destinationInBounds) {
//        listOf("UberX", "UberXL", "UberBlack", "Ola Mini", "Ola Prime", "Ola SUV")
//    } else {
//        listOf("UberBlack", "Ola SUV")
//    }
//
//    return vehicleTypes.map {
//        val traffic = trafficConditions.random()
//        val price = calculatePrice(it, traffic, distance)
//        Ride(vehicleType = it, distance = distance, trafficCondition = traffic, price = price)
//    }.sortedBy { it.price }
//}
//
//


// Custom extension to simplify bounding checks
infix fun LatLng.inRangeOf(bounds: Pair<ClosedFloatingPointRange<Double>, ClosedFloatingPointRange<Double>>): Boolean {
    return this.latitude in bounds.first && this.longitude in bounds.second
}

fun calculatePrice(vehicleType: String, traffic: String, distance: Double): Double {
    val baseFare = when (vehicleType) {
        "UberX", "Ola Mini" -> 40.0
        "UberXL", "Ola Prime" -> 60.0
        "UberBlack", "Ola SUV" -> 80.0
        else -> 50.0
    }

    val perKmRate = when (vehicleType) {
        "UberX", "Ola Mini" -> 10.0
        "UberXL", "Ola Prime" -> 14.0
        "UberBlack", "Ola SUV" -> 18.0
        else -> 12.0
    }

    val trafficMultiplier = when (traffic) {
        "Light" -> 1.0
        "Moderate" -> 1.2
        "Heavy" -> 1.5
        else -> 1.0
    }

    return (baseFare + distance * perKmRate) * trafficMultiplier
}


@Composable
fun RideCard(ride: Ride) {
    // Decide colors based on brand
    val isOla = ride.vehicleType.contains("Ola", ignoreCase = true)
    val containerColor = if (isOla) Color(0xFFFFF176) else Color(0xFF212121) // yellow for Ola, dark for Uber
    val textColor = if (isOla) Color.Black else Color.White

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(6.dp)
        , //onClick = {Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val icon: Painter = when (ride.vehicleType) {
                    "UberX", "Ola Mini" -> painterResource(id = R.drawable.truck_svgrepo_com)
                    "UberXL", "Ola Prime" -> painterResource(id = R.drawable.car_svgrepo_com)
                    "UberBlack", "Ola SUV" -> painterResource(id = R.drawable.truck_svgrepo_com)
                    else -> painterResource(id = R.drawable.car_svgrepo_com)
                }

                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = ride.vehicleType,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "₹${ride.price.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Face, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text("${ride.distance.roundToInt()} km", color = Color.Gray)

                Spacer(Modifier.width(16.dp))

                Icon(Icons.Default.Build, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text(ride.trafficCondition, color = Color.Gray)
            }
        }
    }
}


fun calculateDistanceInKm(start: LatLng, end: LatLng): Double {
    val earthRadius = 6371.0 // km

    val dLat = Math.toRadians(end.latitude - start.latitude)
    val dLng = Math.toRadians(end.longitude - start.longitude)

    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(start.latitude)) *
            cos(Math.toRadians(end.latitude)) *
            sin(dLng / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}