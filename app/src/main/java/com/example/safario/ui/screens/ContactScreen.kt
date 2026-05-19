package com.example.safario.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Shield
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
import com.example.safario.data.model.AppDatabase
import com.example.safario.data.model.Contact
import com.example.safario.utils.ContactStorage
import kotlinx.coroutines.launch

@Composable
fun ContactScreen() {

    val context = LocalContext.current

    val database =
        remember {
            AppDatabase.getDatabase(context)
        }

    val contactDao =
        remember {
            database.contactDao()
        }

    val scope = rememberCoroutineScope()

    var contacts by remember {
        mutableStateOf<List<Contact>>(emptyList())
    }

    var emergencyContacts by remember {
        mutableStateOf(
            ContactStorage.getEmergencyContacts(context)
        )
    }

    var name by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    // LOAD CONTACTS
    LaunchedEffect(Unit) {

        contacts =
            contactDao.getAllContacts()
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "CONTACTS",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ADD CONTACT CARD
                Card(
                    shape = RoundedCornerShape(24.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF24114D)
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {

                        OutlinedTextField(
                            value = name,

                            onValueChange = {
                                name = it
                            },

                            label = {
                                Text("Name")
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        OutlinedTextField(
                            value = phone,

                            onValueChange = {
                                phone = it
                            },

                            label = {
                                Text("Phone")
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        Button(
                            onClick = {

                                if (
                                    name.isNotBlank()
                                    &&
                                    phone.isNotBlank()
                                ) {

                                    scope.launch {

                                        val newContact =
                                            Contact(
                                                name = name,
                                                phone = phone
                                            )

                                        contactDao.insert(
                                            newContact
                                        )

                                        contacts =
                                            contactDao
                                                .getAllContacts()

                                        name = ""
                                        phone = ""
                                    }
                                }
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFF8B5CF6)
                                )
                        ) {

                            Text("Save Contact")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Saved Contacts",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(18.dp))
            }

            items(contacts) { contact ->

                val isEmergency =
                    emergencyContacts.any {
                        it.phone == contact.phone
                    }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),

                    shape = RoundedCornerShape(24.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF24114D)
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
                                .size(58.dp)
                                .background(
                                    Color(0xFF8B5CF6),
                                    CircleShape
                                ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Person,

                                contentDescription = null,

                                tint = Color.White
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(16.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = contact.name,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text = contact.phone,
                                color = Color(0xFFC8C2E6),
                                fontSize = 16.sp
                            )
                        }

                        Row {

                            // CALL
                            IconButton(
                                onClick = {

                                    val intent =
                                        Intent(
                                            Intent.ACTION_DIAL
                                        ).apply {

                                            data =
                                                Uri.parse(
                                                    "tel:${contact.phone}"
                                                )
                                        }

                                    context.startActivity(intent)
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Call,

                                    contentDescription = null,

                                    tint =
                                        Color(0xFF22C55E)
                                )
                            }

                            // EMERGENCY
                            IconButton(
                                onClick = {

                                    emergencyContacts =
                                        if (isEmergency) {

                                            emergencyContacts
                                                .filter {
                                                    it.phone !=
                                                            contact.phone
                                                }

                                        } else {

                                            emergencyContacts +
                                                    contact
                                        }

                                    ContactStorage
                                        .saveEmergencyContacts(
                                            context,
                                            emergencyContacts
                                        )
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (isEmergency)
                                            Icons.Default.Shield
                                        else
                                            Icons.Outlined.Shield,

                                    contentDescription = null,

                                    tint =
                                        if (isEmergency)
                                            Color.Red
                                        else
                                            Color.White
                                )
                            }

                            // DELETE
                            IconButton(
                                onClick = {

                                    scope.launch {

                                        contactDao.delete(
                                            contact
                                        )

                                        contacts =
                                            contactDao
                                                .getAllContacts()
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Delete,

                                    contentDescription = null,

                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }

            item {

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}