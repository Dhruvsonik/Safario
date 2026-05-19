package com.example.safario.viewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.safario.viewModel.ui.theme.SafarioTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

class MainViewModel : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen()

        }
    }
}


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Safario",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Navigate to Map */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Safe Routes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* SOS Feature */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SOS Alert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Emergency Contacts */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Emergency Contacts")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SafarioTheme {
        HomeScreen()
    }
}