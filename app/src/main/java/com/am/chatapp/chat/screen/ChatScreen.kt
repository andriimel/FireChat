package com.am.chatapp.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.am.chatapp.chat.model.Message
import com.am.chatapp.chat.viewmodel.ChatViewModel
import com.am.chatapp.presentation.auth.AuthViewModel
import com.am.chatapp.presentation.screens.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(onLogoutClick: () -> Unit) {

    TopAppBar(
        title = {
            Text(text = "Chat App")
        },
        actions = {

            IconButton(onClick = { onLogoutClick() }) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit

) {
    val messages = viewModel.messages.collectAsState(initial = emptyList()).value
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.GroupChat.route) { inclusive = true }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ChatTopBar(onLogoutClick = {
            authViewModel.logOutUser()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.GroupChat.route) { inclusive = true }
            }

        })
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                val isCurrentUser = message.senderId == viewModel.currentUserId
                MessageItem(message = message, isCurrentUser = isCurrentUser)
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.weight(1f)
                    .onFocusChanged{
                        focusState ->
                        if (focusState.isFocused) {
                            keyboardController?.show()
                        }
                    },
                placeholder = { Text("Enter message...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (textState.text.isNotBlank()) {
                    viewModel.sendMessage(textState.text)
                    textState = TextFieldValue("")
                }
            }) {
                Text("Send")
            }
        }
    }
}


@Composable
fun MessageItem(message: Message, isCurrentUser: Boolean) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {

            Text(
                text = if (isCurrentUser) "You" else message.senderName.ifEmpty { "Anonymous" },
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 2.dp)
            )


        Box(
            modifier = Modifier
                .background(
                    if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFEDEDED),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(text = message.content, fontSize = 16.sp)
        }
    }
}


fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}