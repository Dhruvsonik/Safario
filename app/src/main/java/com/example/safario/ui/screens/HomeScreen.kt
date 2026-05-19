package com.example.safario.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.safario.network.DirectionsRetrofit
import com.example.safario.network.PlaceResult
import com.example.safario.network.WeatherRetrofit
import com.example.safario.utils.SessionManager
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    userEmail: String
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val fusedLocationClient =
        LocationServices
            .getFusedLocationProviderClient(context)

    var cityName by remember {
        mutableStateOf("Loading...")
    }

    var temperature by remember {
        mutableStateOf("--")
    }

    var weatherCondition by remember {
        mutableStateOf("")
    }

    var currentTime by remember {
        mutableStateOf("")
    }

    var nearbyPlaces by remember {
        mutableStateOf<List<PlaceResult>>(emptyList())
    }
    val session = SessionManager(context)

    val userName =
        remember {
            session.getUserName()
        }
    LaunchedEffect(Unit) {

        currentTime =
            SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ).format(Date())

        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@LaunchedEffect
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                location?.let {

                    coroutineScope.launch {

                        try {

                            // CITY
                            val geocoder =
                                Geocoder(
                                    context,
                                    Locale.getDefault()
                                )

                            val addresses =
                                geocoder.getFromLocation(
                                    it.latitude,
                                    it.longitude,
                                    1
                                )

                            cityName =
                                addresses?.firstOrNull()?.locality
                                    ?: "Unknown"

                            // WEATHER
                            val weather =
                                WeatherRetrofit.api
                                    .getWeather(
                                        lat = it.latitude,
                                        lon = it.longitude,
                                        apiKey =
                                            "5fcb21f3edd18d0713bc92610df602ad"
                                    )

                            temperature =
                                "${weather.main.temp.toInt()}°C"

                            weatherCondition =
                                weather.weather.firstOrNull()?.main
                                    ?: ""

                            // REAL NEARBY PLACES
                            val places =
                                DirectionsRetrofit.api
                                    .getNearbyPlaces(
                                        location =
                                            "${it.latitude},${it.longitude}",

                                        radius = 5000,

                                        type = "tourist_attraction",

                                        apiKey = "AIzaSyBOA7ee3CUHyvJkl-PtNszpfnWy0sscbVE"
                                    )

                            nearbyPlaces =
                                places.results.take(3)

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }
                }
            }
    }

    LazyColumn(
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
            .padding(18.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Welcome back, $userName",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "👋",
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =
                    "$cityName • $temperature $weatherCondition",

                color = Color(0xFFC8C2E6),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(26.dp))

            // SAFETY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF24114D)
                ),

                shape = RoundedCornerShape(26.dp)
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Current Area Safety",
                        color = Color(0xFFC8C2E6)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Safe Zone",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    HorizontalDivider(
                        color = Color(0x55FFFFFF)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        InfoItem(
                            "Crime Risk",
                            "Low"
                        )

                        InfoItem(
                            "Crowd Density",
                            "Moderate"
                        )

                        InfoItem(
                            "Time",
                            currentTime
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI CARD
            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF24114D)
                ),

                onClick = {
                    navController.navigate(
                        "ai/$userEmail"
                    )
                }
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.SmartToy,

                        contentDescription = null,

                        tint = Color.Cyan,

                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Ask Safario AI",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Text(
                            text =
                                "Your intelligent travel companion",

                            color = Color(0xFFC8C2E6)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // QUICK ACTIONS
            // QUICK ACTIONS
            Text(
                text = "Quick Actions",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

// FIRST ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                QuickActionCard(
                    title = "Smart Map",
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                ) {

                    navController.navigate(
                        "map/$userEmail"
                    )
                }

                QuickActionCard(
                    title = "SOS Alert",
                    color = Color(0xFFFF2E63),
                    modifier = Modifier.weight(1f)
                ) {

                    navController.navigate(
                        "sos/$userEmail"
                    )
                }

                QuickActionCard(
                    title = "Contacts",
                    color = Color(0xFF06B6D4),
                    modifier = Modifier.weight(1f)
                ) {

                    navController.navigate("contacts")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

// SECOND ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                QuickActionCard(
                    title = "Nearby Police",
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f)
                ) {}

                QuickActionCard(
                    title = "Hospitals",
                    color = Color(0xFF22C55E),
                    modifier = Modifier.weight(1f)
                ) {}

                QuickActionCard(
                    title = "AI Chat",
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                ) {

                    navController.navigate(
                        "ai/$userEmail"
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // RECOMMENDATIONS
            Text(
                text = "Smart Recommendations",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            nearbyPlaces.forEach { place ->

                RecommendationCard(
                    title = place.name,
                    distance = place.vicinity,
                    safety = "Safe",
                    color = Color(0xFF22C55E)
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    value: String
) {

    Column {

        Text(
            text = title,
            color = Color(0xFFC8C2E6),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            color = Color.Cyan,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun QuickActionCard(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(120.dp),

        onClick = onClick,

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF24114D)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),

            verticalArrangement =
                Arrangement.SpaceEvenly,

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color,
                        shape = RoundedCornerShape(16.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "●",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun RecommendationCard(
    title: String,
    distance: String,
    safety: String,
    color: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF24114D)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color,
                        shape = RoundedCornerShape(18.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "🛡",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = distance,
                    color = Color(0xFFC8C2E6)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = safety,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = ">",
                color = Color(0xFFC8C2E6),
                fontSize = 22.sp
            )
        }
    }
}