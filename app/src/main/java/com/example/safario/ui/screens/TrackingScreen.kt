package com.example.safario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.database.database


@Composable
fun TrackingScreen() {

    var trackingId by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<LatLng?>(null) }

    val database = Firebase.database.reference

    val cameraPositionState = rememberCameraPositionState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = trackingId,
            onValueChange = { trackingId = it },
            label = { Text("Enter Tracking ID") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {

            database.child("tracking").child(trackingId)
                .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                    override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                        val lat = snapshot.child("lat").getValue(Double::class.java)
                        val lng = snapshot.child("lng").getValue(Double::class.java)

                        if (lat != null && lng != null) {
                            location = LatLng(lat, lng)

                            cameraPositionState.move(
                                com.google.android.gms.maps.CameraUpdateFactory
                                    .newLatLngZoom(location!!, 15f)
                            )
                        }
                    }

                    override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
                })

        }) {
            Text("Track")
        }

        Spacer(modifier = Modifier.height(16.dp))

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {

            location?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "User Location"
                )
            }
        }
    }
}