package com.example.safario.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.safario.network.DirectionsRetrofit
import com.example.safario.utils.decodePolyline
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun MapScreen(
    navController: NavController,
    userEmail: String,
    lat: Double? = null,
    lng: Double? = null,
    name: String? = null
) {

    val context = LocalContext.current

    val fusedLocationClient =
        LocationServices
            .getFusedLocationProviderClient(context)

    val coroutineScope = rememberCoroutineScope()

    val defaultLocation =
        LatLng(28.6139, 77.2090)

    var currentLocation by remember {
        mutableStateOf(defaultLocation)
    }

    var searchedLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var routePoints by remember {
        mutableStateOf<List<LatLng>>(emptyList())
    }

    var distanceText by remember {
        mutableStateOf("")
    }

    var durationText by remember {
        mutableStateOf("")
    }

    val cameraPositionState =
        rememberCameraPositionState {

            position = CameraPosition.fromLatLngZoom(
                defaultLocation,
                13f
            )
        }

    // GET CURRENT LOCATION
    LaunchedEffect(Unit) {

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

                    currentLocation =
                        LatLng(
                            it.latitude,
                            it.longitude
                        )

                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(
                            currentLocation,
                            14f
                        )
                }
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
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // SEARCH BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = searchText,

                    onValueChange = {
                        searchText = it
                    },

                    modifier = Modifier.weight(1f),

                    placeholder = {
                        Text("Search destination")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector = Icons.Outlined.Map,
                            contentDescription = null
                        )
                    },

                    shape = RoundedCornerShape(20.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedContainerColor =
                                Color(0xCC24114D),

                            unfocusedContainerColor =
                                Color(0xCC24114D),

                            focusedBorderColor =
                                Color(0xFF8B5CF6),

                            unfocusedBorderColor =
                                Color.Transparent,

                            focusedTextColor =
                                Color.White,

                            unfocusedTextColor =
                                Color.White
                        )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // BUTTONS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                // SEARCH
                Button(
                    onClick = {

                        try {

                            val geocoder =
                                Geocoder(
                                    context,
                                    Locale.getDefault()
                                )

                            val addresses =
                                geocoder.getFromLocationName(
                                    searchText,
                                    1
                                )

                            if (!addresses.isNullOrEmpty()) {

                                val address =
                                    addresses[0]

                                searchedLocation =
                                    LatLng(
                                        address.latitude,
                                        address.longitude
                                    )

                                cameraPositionState.position =
                                    CameraPosition.fromLatLngZoom(
                                        searchedLocation!!,
                                        14f
                                    )

                            } else {

                                Toast.makeText(
                                    context,
                                    "Location not found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF8B5CF6)
                    )
                ) {

                    Text("Search")
                }

                // ROUTE
                Button(
                    onClick = {

                        coroutineScope.launch {

                            try {

                                val destination =
                                    searchedLocation
                                        ?: return@launch

                                val response =
                                    DirectionsRetrofit.api
                                        .getDirections(

                                            origin =
                                                "${currentLocation.latitude},${currentLocation.longitude}",

                                            destination =
                                                "${destination.latitude},${destination.longitude}",

                                            apiKey =
                                                "AIzaSyCZJ7mv8GQVMQb4pwok2svb-11XV6UEKdE"
                                        )

                                val route =
                                    response.routes
                                        .firstOrNull()

                                route?.let {

                                    routePoints =
                                        decodePolyline(
                                            it.overview_polyline.points
                                        )

                                    distanceText =
                                        it.legs[0]
                                            .distance.text

                                    durationText =
                                        it.legs[0]
                                            .duration.text
                                }

                            } catch (e: Exception) {

                                e.printStackTrace()

                                Toast.makeText(
                                    context,
                                    "Route failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFFF2E63)
                    )
                ) {

                    Text("Route")
                }
            }

            // ETA CARD
            if (distanceText.isNotEmpty()) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xCC24114D)
                    ),

                    shape = RoundedCornerShape(20.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text =
                                "Distance: $distanceText",

                            color = Color.White
                        )

                        Text(
                            text =
                                "ETA: $durationText",

                            color = Color.Cyan
                        )
                    }
                }
            }

            // MAP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),

                    cameraPositionState =
                        cameraPositionState
                ) {

                    // CURRENT LOCATION
                    Marker(
                        state = MarkerState(
                            position = currentLocation
                        ),

                        title = "You"
                    )

                    // SEARCHED LOCATION
                    searchedLocation?.let {

                        Marker(
                            state = MarkerState(
                                position = it
                            ),

                            title = searchText
                        )
                    }

                    // ROUTE LINE
                    if (routePoints.isNotEmpty()) {

                        Polyline(
                            points = routePoints,
                            color = Color.Blue,
                            width = 12f
                        )
                    }
                }
            }
        }
    }
}