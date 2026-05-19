package com.example.safario.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import com.example.safario.ui.screens.ContactScreen
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Phone
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
import androidx.navigation.NavController
import com.example.safario.ui.components.BottomNavBar
import com.example.safario.utils.ContactStorage
import com.example.safario.utils.HistoryStorage
import com.example.safario.utils.SessionManager
import kotlinx.coroutines.launch

enum class Tab {
    PROFILE,
    SETTINGS,
    HELP,
    CONTACTS,
    HISTORY,
    LOGOUT
}

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    rootNavController: NavController
) {

    val context = LocalContext.current

    val session = SessionManager(context)

    val userEmail =
        session.getUserEmail() ?: ""

    val drawerState =
        rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var tab by remember {
        mutableStateOf(Tab.PROFILE)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF1C103D)
            ) {

                Spacer(modifier = Modifier.height(30.dp))

                DrawerMenuItem(
                    "Profile",
                    Icons.Default.Person,
                    tab == Tab.PROFILE
                ) {

                    tab = Tab.PROFILE

                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerMenuItem(
                    "Settings",
                    Icons.Default.Settings,
                    tab == Tab.SETTINGS
                ) {

                    tab = Tab.SETTINGS

                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerMenuItem(
                    "Help",
                    Icons.Default.Help,
                    tab == Tab.HELP
                ) {

                    tab = Tab.HELP

                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerMenuItem(
                    "Contacts",
                    Icons.Default.Phone,
                    tab == Tab.CONTACTS
                ) {

                    tab = Tab.CONTACTS

                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerMenuItem(
                    "History",
                    Icons.Default.History,
                    tab == Tab.HISTORY
                ) {

                    tab = Tab.HISTORY

                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerMenuItem(
                    "Logout",
                    Icons.Default.Logout,
                    tab == Tab.LOGOUT
                ) {

                    tab = Tab.LOGOUT

                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        }
    ) {

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
                    .padding(18.dp)
            ) {

                Spacer(modifier = Modifier.height(28.dp))

                // TOP BAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {

                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = tab.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                when (tab) {

                    Tab.PROFILE -> ModernProfileContent()

                    Tab.SETTINGS -> ModernSettingsContent(
                        isDarkMode,
                        onThemeChange
                    ) {
                        tab = Tab.PROFILE
                    }

                    Tab.HELP -> ModernHelpContent()

                    Tab.CONTACTS -> ContactScreen()

                    Tab.HISTORY -> ModernHistoryContent(
                        rootNavController
                    )

                    Tab.LOGOUT -> ModernLogoutContent {
                        onLogout()
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }


        }
    }
}

@Composable
fun DrawerMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {

    NavigationDrawerItem(
        label = {
            Text(
                title,
                color = Color.White
            )
        },

        selected = selected,

        onClick = onClick,

        icon = {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
        },

        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFF8B5CF6)
        )
    )
}

@Composable
fun ModernProfileContent() {

    val context = LocalContext.current

    val session = SessionManager(context)

    Column {

        // PROFILE CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
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

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            Color(0xFF8B5CF6),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = session.getUserName() ?: "Tourist",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = session.getUserEmail() ?: "",
                    color = Color(0xFFB8B5C8),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // DIGITAL ID CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF8B5CF6)
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Digital Tourist ID",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Blockchain Verified ✓",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                

                Text(
                    text = "Status: Active",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ModernSettingsContent(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onSaved: () -> Unit
) {

    val context = LocalContext.current

    val session = SessionManager(context)

    var name by remember {
        mutableStateOf(
            session.getUserName() ?: ""
        )
    }

    var email by remember {
        mutableStateOf(
            session.getUserEmail() ?: ""
        )
    }

    var password by remember {
        mutableStateOf("")
    }

    LazyColumn {

        item {

            ModernTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = "Name"
            )

            Spacer(modifier = Modifier.height(14.dp))

            ModernTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(14.dp))

            ModernTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = "Password"
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                        .padding(20.dp),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        "Dark Mode",
                        color = Color.White
                    )

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onThemeChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    session.updateUserName(name)
                    session.updateUserEmail(email)

                    if (password.isNotEmpty()) {
                        session.updatePassword(password)
                    }

                    Toast.makeText(
                        context,
                        "Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    onSaved()

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                )
            ) {

                Text("Save Changes")
            }
        }
    }
}

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
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
}

@Composable
fun ModernHelpContent() {

    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }

    var issue by remember {
        mutableStateOf("")
    }

    Column {

        ModernTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = "Your Email"
        )

        Spacer(modifier = Modifier.height(14.dp))

        ModernTextField(
            value = issue,
            onValueChange = {
                issue = it
            },
            label = "Describe Issue"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "Issue Submitted",
                    Toast.LENGTH_SHORT
                ).show()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B5CF6)
            )
        ) {

            Text("Send")
        }
    }
}



@Composable
fun ModernHistoryContent(
    rootNavController: NavController
) {

    val context = LocalContext.current

    val history =
        HistoryStorage.getHistory(context)

    LazyColumn {

        items(history) { place ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF24114D)
                ),

                onClick = {

                    rootNavController.navigate(
                        "map?name=$place"
                    )
                }
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = place,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tap to open location",
                        color = Color(0xFFB8B5C8)
                    )
                }
            }
        }
    }
}

@Composable
fun ModernLogoutContent(
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Logout from Safario?",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF2E63)
            )
        ) {

            Text("Logout")
        }
    }
}