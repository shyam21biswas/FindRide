package com.example.place3mapbar





import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.example.place3mapbar.DataStoreHelper.getUserName
import com.example.place3mapbar.DataStoreHelper.saveUserName
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//splash screnn..........................

@Composable
fun SplashScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(4000)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate("welcome") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("signup") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fullscreen Background Image
        Image(
            painter = painterResource(id = R.drawable.mymainpage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Foreground content (logo + app name)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


            }
        }
    }
}


var Name: String = ""

@Composable
fun SignupScreen1n(navController: NavController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val oneTapClient = Identity.getSignInClient(context)
    var userName by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task: Task<*> ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            userName = currentUser?.displayName
                            Name = userName.toString()?.substringBefore(" ") ?: ""
                            scope.launch {
                                saveUserName(context, Name)
                            }






                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Sign in failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userName == null) {
                Text(
                    text = "Join Us!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        val signInRequest = BeginSignInRequest.builder()
                            .setGoogleIdTokenRequestOptions(
                                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                    .setSupported(true)
                                    .setServerClientId("608460382093-kg5gavld4k43v300e5qsufju4m1n57bt.apps.googleusercontent.com")
                                    .setFilterByAuthorizedAccounts(false)
                                    .build()
                            )
                            .build()

                        oneTapClient.beginSignIn(signInRequest)
                            .addOnSuccessListener { result ->
                                val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                                launcher.launch(intentSenderRequest)
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                            }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4285F4),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(text = "Sign in with Google", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Sign in with email",
                    modifier = Modifier.clickable { navController.navigate("manualEmail") },
                    color = Color(0xFF4F76BB)
                )
            } else {

                Text(
                    text = "Welcome, $userName!!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                LaunchedEffect(Unit) {
                    delay(4000)
                    navController.navigate("mine")
                    {
                        popUpTo("signup") { inclusive = true }
                    }

                }




            }
        }
    }
}


@Composable
fun Welcome(navController: NavController) {
    val context = LocalContext.current
    val storedUserName by DataStoreHelper.getUserName(context).collectAsState(initial = null)

    // Define colors
    val backgroundColor = Color(0xFFFAFAFA) // Soft white
    val redPinkColor = Color(0xFFE91E63) // Red-pink

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back,\n${storedUserName ?: "User"}!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = redPinkColor,
                textAlign = TextAlign.Center
            )

            LaunchedEffect(Unit) {
                delay(2000)
                navController.navigate("mine")


            }


        }
    }
}




@Composable
fun mainr(navController: NavController){
    val context = LocalContext.current
    val storedUserName by DataStoreHelper.getUserName(context).collectAsState(initial = null)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("VEDANSHI $storedUserName",fontSize = 28.sp, color = Color.White)
        }
    }


}
