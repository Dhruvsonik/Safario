package com.example.safario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safario.network.ChatRequest
import com.example.safario.network.Message
import com.example.safario.network.RetrofitInstance
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale

@Composable
fun AIChatScreen(
    navController: NavController,
    userEmail: String
) {

    val coroutineScope = rememberCoroutineScope()

    var userMessage by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val fusedLocationClient =
        LocationServices
            .getFusedLocationProviderClient(context)

    var userLocation by remember {
        mutableStateOf("Unknown")
    }
    var messages by remember {
        mutableStateOf(
            listOf(
                Pair(
                    "Safario AI",
                    "Hello Dhruv 👋\nI’m your intelligent travel safety companion."
                )
            )
        )
    }

    var isLoading by remember {
        mutableStateOf(false)
    }
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

                    try {

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

                        userLocation =
                            addresses
                                ?.firstOrNull()
                                ?.locality
                                ?: "Unknown"

                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }
            }
    }

    fun sendMessage() {

        val messageText = userMessage.trim()

        if (messageText.isEmpty()) return

        messages =
            messages + Pair("You", messageText)

        userMessage = ""

        isLoading = true

        coroutineScope.launch {

            try {

                val response =
                    RetrofitInstance.api.getChatResponse(

                        authorization =
                            "Bearer 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",

                        request = ChatRequest(

                            model =
                                "openai/gpt-3.5-turbo",

                            messages = listOf(

                                Message(
                                    role = "system",
                                    content =
                                        """
You are Safario AI.

The user is currently in:
$userLocation

Always answer according to the user's current location.

If the user asks:
- restaurants
- cafes
- hospitals
- police
- tourist places
- routes
- hotels

then provide suggestions near their current location automatically.

Do not ask user for their city again.

Only help with:
- tourism
- travel safety
- nearby places
- routes
- emergency help
- translation
- travel guidance
""".trimIndent()
                                ),

                                Message(
                                    role = "user",
                                    content = messageText
                                )
                            )
                        )
                    )

                val aiReply =
                    response.choices
                        .firstOrNull()
                        ?.message
                        ?.content
                        ?: "No response"

                messages =
                    messages + Pair(
                        "Safario AI",
                        aiReply
                    )

            } catch (e: Exception) {

                messages =
                    messages + Pair(
                        "Safario AI",
                        "⚠ Error: ${e.message}"
                    )
            }

            isLoading = false
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
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 24.dp,
                    bottom = 110.dp
                )
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            // HEADER
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF06B6D4),
                                    Color(0xFF8B5CF6)
                                )
                            ),
                            RoundedCornerShape(18.dp)
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.AutoAwesome,

                        contentDescription = null,

                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "Safario AI",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Online • Powered by AI",
                        color = Color(0xFF22C55E),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // QUICK SUGGESTIONS
            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                SuggestionCard(
                    "Is this area safe at night?"
                ) {

                    userMessage =
                        "Is this area safe at night?"
                }

                SuggestionCard(
                    "Find safe cafes nearby"
                ) {

                    userMessage =
                        "Find safe cafes nearby"
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                SuggestionCard(
                    "Translate Help to Hindi"
                ) {

                    userMessage =
                        "Translate help me into Hindi"
                }

                SuggestionCard(
                    "Safest route to hotel"
                ) {

                    userMessage =
                        "Find safest route to my hotel"
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CHAT LIST
            LazyColumn(
                modifier = Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                items(messages) { message ->

                    val isUser =
                        message.first == "You"

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            if (isUser)
                                Arrangement.End
                            else
                                Arrangement.Start
                    ) {

                        Card(
                            modifier = Modifier.widthIn(
                                max = 300.dp
                            ),

                            shape = RoundedCornerShape(22.dp),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        if (isUser)
                                            Color(0xFF8B5CF6)
                                        else
                                            Color(0xFF24114D)
                                )
                        ) {

                            Text(
                                text = message.second,

                                color = Color.White,

                                modifier = Modifier
                                    .padding(16.dp),

                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                if (isLoading) {

                    item {

                        Card(
                            shape = RoundedCornerShape(20.dp),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        Color(0xFF24114D)
                                )
                        ) {

                            Text(
                                text = "Safario AI is typing...",
                                color = Color.White,

                                modifier = Modifier
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // BOTTOM INPUT BAR
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFF140B35))
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF24114D),
                        RoundedCornerShape(30.dp)
                    )
                    .padding(
                        horizontal = 18.dp,
                        vertical = 10.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                TextField(
                    value = userMessage,

                    onValueChange = {
                        userMessage = it
                    },

                    modifier = Modifier.weight(1f),

                    placeholder = {

                        Text(
                            "Ask Safario AI anything..."
                        )
                    },

                    colors =
                        TextFieldDefaults.colors(

                            focusedContainerColor =
                                Color.Transparent,

                            unfocusedContainerColor =
                                Color.Transparent,

                            disabledContainerColor =
                                Color.Transparent,

                            focusedIndicatorColor =
                                Color.Transparent,

                            unfocusedIndicatorColor =
                                Color.Transparent,

                            cursorColor = Color.White
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {

                        if (
                            userMessage.isNotBlank()
                        ) {
                            sendMessage()
                        }
                    },

                    shape = RoundedCornerShape(50),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF8B5CF6)
                        )
                ) {

                    Text(
                        text = "➤",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionCard(
    title: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(170.dp)
            .height(90.dp),

        onClick = onClick,

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF24114D)
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            contentAlignment =
                Alignment.CenterStart
        ) {

            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}