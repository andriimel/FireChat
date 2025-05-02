package com.am.chatapp.presentation.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.am.chatapp.domain.repository.AuthResult

@Composable
fun ShowAuthResultDialog(
    showDialog: Boolean,
    authResult: AuthResult,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        if (authResult is AuthResult.Success || authResult is AuthResult.Error) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = if (authResult is AuthResult.Success) "Success" else "Error")
                },
                text = {
                    Text(
                        text = (authResult as? AuthResult.Success)?.message
                            ?: (authResult as? AuthResult.Error)?.message.orEmpty()
                    )
                },
                confirmButton = {
                    TextButton(onClick = onDismiss) {

                        Text("OK")
                    }
                }
            )
        }
    }
}