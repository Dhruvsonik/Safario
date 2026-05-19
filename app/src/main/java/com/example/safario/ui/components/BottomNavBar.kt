package com.example.safario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String,
    userEmail: String
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 14.dp,
                vertical = 10.dp
            )
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(30.dp)
            ),

        shape = RoundedCornerShape(30.dp),

        color = Color.Transparent
    ) {

        Row(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF24114D),
                            Color(0xFF1A103A)
                        )
                    )
                )
                .padding(
                    horizontal = 4.dp,
                    vertical = 8.dp
                ),

            horizontalArrangement =
                Arrangement.SpaceEvenly,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // HOME
            BottomNavItem(
                title = "Home",
                icon = Icons.Outlined.Home,
                selected = currentRoute == "home",
            ) {

                navController.navigate(
                    "home/$userEmail"
                ) {
                    launchSingleTop = true
                }
            }

            // MAP
            BottomNavItem(
                title = "Map",
                icon = Icons.Outlined.Map,
                selected = currentRoute == "map",
            ) {

                navController.navigate(
                    "map/$userEmail"
                ) {
                    launchSingleTop = true
                }
            }

            // SOS
            BottomNavItem(
                title = "SOS",
                icon = Icons.Outlined.Warning,
                selected = currentRoute == "sos",
                selectedColor = Color(0xFFFF2E63),
            ) {

                navController.navigate(
                    "sos/$userEmail"
                ) {
                    launchSingleTop = true
                }
            }

            // AI
            BottomNavItem(
                title = "AI",
                icon = Icons.Outlined.SmartToy,
                selected = currentRoute == "ai",
            ) {

                navController.navigate(
                    "ai/$userEmail"
                ) {
                    launchSingleTop = true
                }
            }

            // PROFILE
            BottomNavItem(
                title = "Profile",
                icon = Icons.Outlined.Person,
                selected = currentRoute == "profile",
            ) {

                navController.navigate(
                    "profile/$userEmail"
                ) {
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    selectedColor: Color = Color(0xFF8B5CF6),
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .width(58.dp)
            .clickable {
                onClick()
            },

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .background(
                    color =
                        if (selected)
                            selectedColor
                        else
                            Color.Transparent,

                    shape = RoundedCornerShape(18.dp)
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                ),

            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,

                tint =
                    if (selected)
                        Color.White
                    else
                        Color(0xFFC9C4E2)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,

            color =
                if (selected)
                    Color.White
                else
                    Color(0xFFC9C4E2),

            fontSize = 9.sp,

            maxLines = 1,

            overflow = TextOverflow.Ellipsis,

            fontWeight =
                if (selected)
                    FontWeight.SemiBold
                else
                    FontWeight.Normal,

            style = MaterialTheme.typography.labelSmall
        )
    }
}