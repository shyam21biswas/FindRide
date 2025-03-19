package com.example.place3mapbar




import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDemo() {
    var showBottomSheet by remember { mutableStateOf(false) }

    // ✅ Button to Open Bottom Sheet
    Button(onClick = { showBottomSheet = true }) {
        Text("Show Bottom Sheet")
    }

    // ✅ Half-Screen Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },

        ) {
            asd()
            // 📌 Bottom Sheet Content
            /*Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("This is a Bottom Sheet", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { showBottomSheet = false }) {
                    Text("Close")
                }

                Spacer(modifier = Modifier.height(20.dp))
            }*/
        }
    }
}
@Composable
fun BottomNavScree() {

        Column(
            modifier = Modifier.padding(2.dp)
        ) {
            BottomSheetDemo() // ✅ Add this anywhere inside your screen
        }
    }


@Composable
fun asd(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is a Bottom Sheet", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))



        Spacer(modifier = Modifier.height(20.dp))
    }
}
