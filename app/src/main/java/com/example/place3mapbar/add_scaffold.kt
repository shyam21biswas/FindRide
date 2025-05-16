package com.example.place3mapbar



import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource


import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)


@Composable
fun BottomNavScreen() {
    val navController = rememberNavController()
    val rideViewModel: RideViewModel = viewModel() // scoped to NavigationGraph

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { slideInHorizontally() },
            popExitTransition = { slideOutHorizontally() }
        ) {
            composable("home") { HomeScreen(navController) }
            composable("ride") { RideScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(

        BottomNavItem("Ride", R.drawable.baseline_local_car_wash_24, "ride"),
        BottomNavItem("Home", R.drawable.baseline_home_24, "home"),
        BottomNavItem("Profile", R.drawable.baseline_manage_accounts_24, "profile")
    )
    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
//                onClick = {
//                    if (currentRoute != item.route) {
//                        navController.navigate(item.route) {
//                            launchSingleTop = true
//                        }
//                    }
//                }
                onClick = {
                    val isOnTop = navController.currentBackStackEntry?.destination?.route == item.route
                    if (!isOnTop) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                enabled = currentRoute != item.route, // 👈 disables tap when already on it
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )

        }
    }
}

//@Composable
//fun HomeScreen(navController: NavController) {
//    Text(text = "🏠 Home Screen")
//
//
//
//    val mapViewModel = MapViewModel()
//    MapScreen(
//       mapViewModel , navController = rememberNavController()
//     )
//}

@Composable
fun HomeScreen(navController: NavController) {
    val mapViewModel = remember { MapViewModel() } // or use: viewModel()

    // Optionally show some header
    Column {
        Text(
            text = "🏠 Home Screen",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )


        MapScreen(
            mapViewModel = mapViewModel,
            navController = navController // ✅ pass main navController
        )
    }
}


@Composable
fun RideScreen() { //Text(text = "🚗 Ride Screen")
    //GoogleMapWithRoutes("AIzaSyBmOX8MxQo37oCgKuO1lMF0saxMoUx6GKU" , pickupLocation.value , destinationLocation.value)

        RideApp()

}
// we have connect api to show a lazy column.....

@Composable
fun ProfileScreen()
{
    Text(text = "👤 Profile Screen")
    Spacer(modifier = Modifier.padding(60.dp))
    Profile()
}

data class BottomNavItem(val label: String, val icon: Int, val route: String)

fun currentRoute(navController: NavController): String? {
    return navController.currentBackStackEntry?.destination?.route
}


