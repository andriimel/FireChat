package com.am.chatapp.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.am.chatapp.presentation.auth.AuthViewModel
import com.am.chatapp.ui.theme.btnBackgroundColor
import com.am.chatapp.ui.theme.mainTextColor
import com.am.chatapp.ui.theme.textfieldBorderColor
import com.am.chatapp.ui.theme.textfieldTextColor
import androidx.compose.runtime.*
import com.am.chatapp.domain.repository.AuthResult

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val orientation = configuration.orientation


    val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT


    val topPadding = if (isPortrait) screenHeight * 0.15f else screenHeight * 0.05f
    val fieldSpacing = screenHeight * 0.03f
    val buttonSpacing = screenHeight * 0.05f

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val authResult by authViewModel.authResult.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    var showDialog by remember {mutableStateOf(false)}

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn){
            navController.navigate("register")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = topPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Log in",
            fontSize = 32.sp,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            color = mainTextColor
        )
        Spacer(modifier = Modifier.height(topPadding))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email", color = textfieldTextColor) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = textfieldBorderColor,
                unfocusedBorderColor = textfieldBorderColor,
                cursorColor = textfieldTextColor
            ),
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(fieldSpacing))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password", color = textfieldTextColor) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = textfieldBorderColor,
                unfocusedBorderColor = textfieldBorderColor,
                cursorColor = textfieldTextColor
            ),
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(buttonSpacing))

        // Log In button
        Button(
            onClick = { authViewModel.loginUserEmailPassword(email.value, password.value)},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = btnBackgroundColor
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("LOG IN", fontSize = 24.sp)
        }
        LaunchedEffect(authResult) {
            if (authResult is AuthResult.Error) {
                showDialog = true
            }
        }

        ShowAuthResultDialog(
            showDialog = showDialog,
            authResult = authResult,
            onDismiss = { showDialog = false
                authViewModel.resetAuthResult()
            }
        )
        //
        TextButton(
            onClick = { navController.navigate(Screen.Register.route) },
                    modifier = Modifier
                    .fillMaxWidth()
        ) {
            Text(
                text = "Don't have an account? Sign up",
                color = mainTextColor,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

