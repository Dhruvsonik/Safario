import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safario.utils.SessionManager

fun isValidPassword(password: String): Boolean {

    val regex =
        Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*]).{6,}$")

    return regex.matches(password)
}

@Composable
fun SignupScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val session = SessionManager(context)

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // LOGO
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFF8B5CF6),
                                Color(0xFF00C2FF)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // TITLE
            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start your smart and safe journey with Safario",
                color = Color(0xFFB8B5C8),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xCC24114D)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    // NAME
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        placeholder = {
                            Text("Full Name")
                        },

                        singleLine = true,

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6)
                            )
                        },

                        shape = RoundedCornerShape(22.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1B1038),
                            unfocusedContainerColor = Color(0xFF1B1038),

                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = Color(0xFF4B3A77),

                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // EMAIL
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        placeholder = {
                            Text("Email")
                        },

                        singleLine = true,

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6)
                            )
                        },

                        shape = RoundedCornerShape(22.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1B1038),
                            unfocusedContainerColor = Color(0xFF1B1038),

                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = Color(0xFF4B3A77),

                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PASSWORD
                    OutlinedTextField(
                        value = password,
                        onValueChange = {

                            password = it
                            error = ""
                        },

                        modifier = Modifier.fillMaxWidth(),

                        placeholder = {
                            Text("Password")
                        },

                        singleLine = true,

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6)
                            )
                        },

                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,

                                    contentDescription = null,

                                    tint = Color.Gray
                                )
                            }
                        },

                        shape = RoundedCornerShape(22.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1B1038),
                            unfocusedContainerColor = Color(0xFF1B1038),

                            focusedBorderColor = Color(0xFF8B5CF6),
                            unfocusedBorderColor = Color(0xFF4B3A77),

                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,

                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ERROR
                    if (error.isNotEmpty()) {

                        Text(
                            text = error,
                            color = Color(0xFFFF2E63),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // SIGNUP BUTTON
                    Button(
                        onClick = {

                            when {

                                name.isEmpty() ||
                                        email.isEmpty() ||
                                        password.isEmpty() -> {

                                    error =
                                        "All fields are required"
                                }

                                !Patterns.EMAIL_ADDRESS
                                    .matcher(email)
                                    .matches() -> {

                                    error =
                                        "Invalid email format"
                                }

                                !isValidPassword(password) -> {

                                    error =
                                        "Password must contain:\n• 1 capital letter\n• 1 special character\n• Minimum 6 characters"
                                }

                                session.isUserRegistered(email) -> {

                                    error =
                                        "User already exists"
                                }

                                else -> {

                                    error = ""

                                    // REGISTER USER
                                    session.registerUser(
                                        email,
                                        password,
                                        name
                                    )

                                    // SAVE LOGIN SESSION
                                    session.saveUserSession(
                                        email,
                                        name
                                    )

                                    navController.navigate(
                                        "main/$email"
                                    ) {

                                        popUpTo("signup") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),

                        shape = RoundedCornerShape(22.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B5CF6)
                        )
                    ) {

                        Text(
                            text = "CREATE ACCOUNT",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // LOGIN
                    TextButton(
                        onClick = {

                            navController.navigate("login")
                        },
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        )
                    ) {

                        Text(
                            text = "Already have an account? Login",
                            color = Color(0xFFB8B5C8)
                        )
                    }
                }
            }
        }
    }
}