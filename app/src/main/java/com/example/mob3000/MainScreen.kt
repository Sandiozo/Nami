package com.example.mob3000

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mob3000.database.UserViewModel

@Composable
fun MainScreen(vm: UserViewModel) {
    val rawUsername = vm.user?.username
    val username = rawUsername?.takeIf { it.isNotBlank() }
    val showDialog = vm.showNameDialog || username == null

    when {
        showDialog -> {
            var text by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = {},
                title = { Text("Welcome") },
                text = {
                    Column {
                        Text("What is your name?")
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            singleLine = true,
                            label = { Text("Name") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (text.isNotBlank()) {
                                vm.updateUsername(text.trim())
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }

        else -> {
            Navigation(vm = vm)
        }
    }
}
