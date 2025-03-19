package com.example.place3mapbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.place3mapbar.Auth.AuthScreen
import com.example.place3mapbar.Auth.AuthViewModel
import com.example.place3mapbar.ui.theme.Place3mapbarTheme
import com.google.android.libraries.places.api.Places
import com.strongtogether.googlemapsjetpackcompose.utils.ManifestUtils
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //forget where it use for now in clg after add auth screen // Retrieve the API key from the manifest file
        val apiKey = ManifestUtils.getApiKeyFromManifest(this)
        // Initialize the Places API with the retrieved API key
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }

        val authViewModel = AuthViewModel(this)
        var startDestination by mutableStateOf("auth")

        // Check Login State
        runBlocking {
            if (authViewModel.isLoggedIn()) {
                startDestination = "main"
            }
        }
        enableEdgeToEdge()
        setContent {
            Place3mapbarTheme {
                val mapViewModel = MapViewModel()
                //MapScreen(
                //    mapViewModel
               // )
                //GoogleMapWithRoute()

               // BottomNavScreen()

                val navController = rememberNavController()

                NavHost(navController, startDestination = startDestination) {
                    composable("auth") { AuthScreen(navController, authViewModel) }
                    composable("main") { BottomNavScreen() } // ✅ Using your Scaffold function
                }
                //BottomSheetDemo()
                //BottomNavScree()

            }
        }
    }
}

//AIzaSyBbW_UEhUa4siydX7A1JWc-NFmRqVgTYVo