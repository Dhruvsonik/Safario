package com.example.safario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safario.R
import com.example.safario.utils.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val session = SessionManager(context)

    LaunchedEffect(Unit) {

        delay(2500)

        val email = session.getUserEmail()

        if (
            session.isLoggedIn()
            &&
            !email.isNullOrEmpty()
        ) {

            navController.navigate("main/$email") {
                popUpTo("splash") {
                    inclusive = true
                }
            }

        } else {

            navController.navigate("login") {
                popUpTo("splash") {
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF050014),
                        Color(0xFF12052E),
                        Color(0xFF1D0F4F)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // GLOW
            Box(
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF6D5DF6)
                                        .copy(alpha = 0.45f),

                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // OUTER BORDER CIRCLE
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF9B5CFF),
                                    Color(0xFF36CFFF)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    // INNER WHITE CIRCLE
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.safario1
                            ),

                            contentDescription = "Safario Logo",

                            modifier = Modifier
                                .size(78.dp)
                                .clip(CircleShape),

                            contentScale =
                                ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            Text(
                text = "Safario",

                color = Color.White,

                fontSize = 34.sp,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Smart Tourist Safety System",

                color = Color(0xFFC9C2E8),

                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Text(
                text = "Securing your journey...",

                color = Color(0xFF9B5CFF),

                fontSize = 18.sp
            )
        }
    }
}