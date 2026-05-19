package com.example.safario.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safario.ui.components.BottomNavBar
import com.google.android.gms.location.LocationServices

@Composable
fun SosScreen(
    navController: NavController,
    userEmail: String
) {

    val context = LocalContext.current

    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var locationText by remember {
        mutableStateOf("Fetching location...")
    }

    // LOCATION
    LaunchedEffect(Unit) {

        try {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    location?.let {

                        val lat = it.latitude
                        val lng = it.longitude

                        locationText =
                            "https://maps.google.com/?q=$lat,$lng"
                    }
                }

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF090014),
                        Color(0xFF140B35),
                        Color(0xFF1D104A)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Emergency SOS",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Press and hold for emergency",
                color = Color(0xFFB8B5C8),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(34.dp))

            // BIG SOS BUTTON
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .shadow(
                        elevation = 40.dp,
                        shape = CircleShape,
                        spotColor = Color.Red
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF2E63),
                                Color(0xFFFF004D)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(70.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "PRESS SOS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ACTION BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                EmergencyActionCard(
                    title = "Call 911",
                    icon = Icons.Default.Call,
                    color = Color(0xFFFF2E63)
                )

                EmergencyActionCard(
                    title = "Video Call",
                    icon = Icons.Default.Videocam,
                    color = Color(0xFF8B5CF6)
                )

                EmergencyActionCard(
                    title = "Voice SOS",
                    icon = Icons.Default.Phone,
                    color = Color(0xFF00C2FF)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // PHONE INPUT
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Emergency Contact Number")
                },
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF24114D),
                    unfocusedContainerColor = Color(0xFF24114D),
                    focusedBorderColor = Color(0xFF8B5CF6),
                    unfocusedBorderColor = Color(0xFF4B3A77),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // SEND SOS BUTTON
            Button(
                onClick = {

                    if (phoneNumber.isEmpty()) {

                        Toast.makeText(
                            context,
                            "Enter phone number",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val message =
                        "🚨 EMERGENCY! I need help.\nMy location:\n$locationText"

                    val intent =
                        Intent(Intent.ACTION_SENDTO).apply {

                            data =
                                Uri.parse("smsto:$phoneNumber")

                            putExtra(
                                "sms_body",
                                message
                            )
                        }

                    context.startActivity(intent)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF2E63)
                )
            ) {

                Text(
                    text = "SEND SOS 🚨",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // CONTACTS SECTION
            Text(
                text = "Emergency Contacts",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            EmergencyContactCard(
                "Sarah Wilson",
                "Emergency Contact",
                "+1 234 567 8900"
            )

            Spacer(modifier = Modifier.height(14.dp))

            EmergencyContactCard(
                "Police Station",
                "Local Authority",
                "100"
            )

            Spacer(modifier = Modifier.height(14.dp))

            EmergencyContactCard(
                "John Doe",
                "Travel Companion",
                "+1 234 567 8901"
            )

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF24114D)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color(0xFF8B5CF6)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "You can also activate SOS by shaking your phone rapidly",
                        color = Color(0xFFB8B5C8),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }


    }
}

@Composable
fun EmergencyActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(110.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF24114D)
        )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color,
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun EmergencyContactCard(
    name: String,
    role: String,
    phone: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF24114D)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        Color(0xFF8B5CF6),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = role,
                    color = Color(0xFFB8B5C8),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = phone,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}