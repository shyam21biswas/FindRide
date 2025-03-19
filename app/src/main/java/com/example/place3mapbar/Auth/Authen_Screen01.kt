package com.example.place3mapbar.Auth

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("user_prefs")

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel) {
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Phone Number Input
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter Phone Number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Button
        Button(
            onClick = {
                viewModel.setLoggedIn(true)
                navController.navigate("main") { popUpTo("auth") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm")
        }
    }
}

// ViewModel to Manage Login State
class AuthViewModel(private val context: Context) : ViewModel() {
    private val dataStore = context.dataStore

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            dataStore.edit { it[booleanPreferencesKey("is_logged_in")] = isLoggedIn }
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val prefs = dataStore.data.first()
        return prefs[booleanPreferencesKey("is_logged_in")] ?: false
    }
}
