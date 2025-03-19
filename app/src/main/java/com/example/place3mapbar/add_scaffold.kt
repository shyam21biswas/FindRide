package com.example.place3mapbar



import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource


import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)


@Composable
fun BottomNavScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = {  fadeIn() },
            exitTransition = {  fadeOut() }

        ) {
            composable("home") { HomeScreen() }
            composable("ride") { RideScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24, "home"),
        BottomNavItem("Ride", R.drawable.baseline_local_car_wash_24, "ride"),
        BottomNavItem("Profile", R.drawable.baseline_manage_accounts_24, "profile")
    )
    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
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

@Composable
fun HomeScreen() {
    Text(text = "🏠 Home Screen")

    val mapViewModel = MapViewModel()
    MapScreen(
       mapViewModel
     )
}

@Composable
fun RideScreen() { Text(text = "🚗 Ride Screen") }

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


