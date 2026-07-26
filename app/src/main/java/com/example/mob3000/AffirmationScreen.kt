package com.example.mob3000

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.database.AffirmationViewModel
import com.example.mob3000.database.CustomAff
import com.example.mob3000.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffirmationScreen(
    onGoHome: () -> Unit
) {
    val context = LocalContext.current
    val vm = remember { AffirmationViewModel(context.applicationContext) }
    val customAffs = vm.customAffirmations

    var newAffText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var affToDelete by remember { mutableStateOf<CustomAff?>(null) }

    GradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Your Affirmations") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalButton(
                            onClick = onGoHome
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Home")
                        }
                    }
                }
            },
            containerColor = Color.Transparent
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .padding(20.dp)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val inputField: @Composable () -> Unit = {
                    OutlinedTextField(
                        value = newAffText,
                        onValueChange = {
                            if (it.length <= 255) {
                                newAffText = it
                                showError = false
                            }
                        },
                        label = { Text("New custom affirmation", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = showError,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (showError) Color.Red else Color.LightGray,
                            unfocusedIndicatorColor = if (showError) Color.Red else Color.LightGray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    if (showError) {
                        Text(
                            text = "Affirmation cannot be empty.",
                            color = Color(0xFFFF6F6F),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    if (newAffText.length == 255) {
                        Text(
                            text = "Max 255 characters reached.",
                            color = Color(0xFFFFC107),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Button(
                        onClick = {
                            val cleaned = newAffText.trim()
                            if (cleaned.isEmpty()) {
                                showError = true
                            } else {
                                vm.addCustomAff(cleaned)
                                newAffText = ""
                                showError = false
                                focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add affirmation")
                    }
                }

                if (isPortrait) {
                    inputField()
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                    if (customAffs.isEmpty()) {
                        Text(
                            text = "No custom affirmations yet.",
                            modifier = Modifier.align(Alignment.Start),
                            color = Color.White
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(customAffs) { aff ->
                                AffirmationRow(aff) { affToDelete = it }
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            inputField()
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            if (customAffs.isEmpty()) {
                                Text(
                                    text = "No saved affirmations.",
                                    color = Color.White
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(customAffs) { aff ->
                                        AffirmationRow(aff) { affToDelete = it }
                                    }
                                }
                            }
                        }
                    }
                }

                affToDelete?.let { aff ->
                    AlertDialog(
                        onDismissRequest = { affToDelete = null },
                        title = { Text("Delete affirmation?") },
                        text = { Text("Are you sure you want to delete this affirmation?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    vm.deleteCustomAff(aff)
                                    affToDelete = null
                                }
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { affToDelete = null }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AffirmationRow(
    aff: CustomAff,
    onDelete: (CustomAff) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = aff.text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onDelete(aff) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete affirmation",
                tint = Color.White
            )
        }
    }
}
