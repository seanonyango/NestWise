package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nestwise.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    userViewModel: UserViewModel,
    onBackToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    val primaryBlue = Color(0xFF1565C0)
    val authError by userViewModel.authError.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("AUD") }
    var passwordVisible by remember { mutableStateOf(false) }

    val currencies = listOf("AUD", "USD", "EUR", "GBP", "NZD")
    var currencyMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Create Account",
                color = primaryBlue,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign up to start tracking your money with NestWise.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(icon, contentDescription = null)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            // Currency dropdown
            ExposedDropdownMenuBox(
                expanded = currencyMenuExpanded,
                onExpandedChange = { currencyMenuExpanded = !currencyMenuExpanded }
            ) {
                OutlinedTextField(
                    value = currency,
                    onValueChange = { },
                    label = { Text("Preferred Currency") },
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = currencyMenuExpanded,
                    onDismissRequest = { currencyMenuExpanded = false }
                ) {
                    currencies.forEach { cur ->
                        DropdownMenuItem(
                            text = { Text(cur) },
                            onClick = {
                                currency = cur
                                currencyMenuExpanded = false
                            }
                        )
                    }
                }
            }

            if (authError != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = authError ?: "",
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.length >= 6) {
                        userViewModel.register(
                            name = name.ifBlank { "User" },
                            email = email,
                            password = password,
                            currency = currency
                        ) {
                            // On success → back to Login
                            onBackToLogin()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Sign Up", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onBackToLogin) {
                Text("Already have an account? Login", color = primaryBlue)
            }
        }
    }
}
