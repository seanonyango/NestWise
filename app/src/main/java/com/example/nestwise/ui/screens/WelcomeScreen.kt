package com.example.nestwise.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    // Define brand colors (Blue and White theme)
    val primaryBlue = Color(0xFF1565C0) // Deep blue
    val backgroundWhite = Color.White

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Placeholder Logo ---
            // Replace with Image(painterResource(id = R.drawable.your_logo), ...) later
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🪺", // Temporary emoji as logo placeholder
                    fontSize = 60.sp
                )
            }

            // --- App Name ---
            Text(
                text = "NestWise",
                color = primaryBlue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- Tagline Placeholder ---
            Text(
                text = "Tagline will be placed here. Tbd",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- Login Button ---
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Login", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Sign Up button ---
            OutlinedButton(
                onClick = onSignUpClick,
                border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Sign Up", color = primaryBlue, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Text(
                text = "© 2025 NestWise. All rights reserved.",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}

