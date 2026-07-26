package com.example.mob3000

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mob3000.database.UserViewModel
import com.example.mob3000.notifications.AffirmationWorker
import com.example.mob3000.ui.theme.GradientBackground
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

// Checks if the notifications are enabled in the system for the app
private fun areNotificationsOn(context: android.content.Context): Boolean {
    val manager = NotificationManagerCompat.from(context)
    val appNotificationsOn = manager.areNotificationsEnabled()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        appNotificationsOn && hasPermission
    } else {
        appNotificationsOn
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val user = userViewModel.user
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val currentInterval = user?.notificationIntervalHours ?: 24
    val homeDefaultsEnabled = user?.defaultAffirmationsHomeEnabled ?: true
    val notifDefaultsEnabled = user?.defaultAffirmationsNotifEnabled ?: true
    val username = user?.username ?: ""

    var editableUsername by remember(username) { mutableStateOf(username) }

    // Checks if system level notifications are on
    var notificationsAvailable by remember {
        mutableStateOf(areNotificationsOn(context))
    }

    LaunchedEffect(Unit) {
        notificationsAvailable = areNotificationsOn(context)
    }

    fun scheduleAffirmationWorker(intervalHours: Int) {
        val request = PeriodicWorkRequestBuilder<AffirmationWorker>(
            intervalHours.toLong(),
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_affirmation",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    GradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomAppBar(containerColor = Color.Transparent) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FilledTonalButton(
                            onClick = {
                                focusManager.clearFocus()
                                onBack()
                            }
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
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                if (isLandscape) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Column(modifier = Modifier.weight(1f)) {

                            OutlinedTextField(
                                value = editableUsername,
                                onValueChange = { editableUsername = it },
                                singleLine = true,
                                label = { Text("Your name", color = Color.White) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.White,
                                    cursorColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.White
                                ),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        val trimmed = editableUsername.trim()
                                        if (trimmed.isBlank()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Name cannot be empty",
                                                    withDismissAction = true
                                                )
                                            }
                                        } else {
                                            userViewModel.updateUsername(trimmed)
                                            focusManager.clearFocus()
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Name updated",
                                                    withDismissAction = true
                                                )
                                            }
                                        }
                                    }
                                ) { Text("Change name") }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Use default affirmations on Home screen",
                                        color = Color.White
                                    )
                                    Text(
                                        "If disabled, only your custom affirmations will appear.",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Switch(
                                    checked = homeDefaultsEnabled,
                                    onCheckedChange = { userViewModel.updateDefaultAffHome(it) }
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Top
                        ) {
                            NotificationSettingsSection(
                                currentIntervalHours = currentInterval,
                                defaultNotifEnabled = notifDefaultsEnabled,
                                isLandscape = true,
                                notificationsAvailable = notificationsAvailable,
                                onIntervalChange = {
                                    userViewModel.updateNotificationInterval(it)
                                    if (notificationsAvailable) {
                                        scheduleAffirmationWorker(it)
                                    }
                                },
                                onDefaultNotifChange = {
                                    userViewModel.updateDefaultAffNotif(it)
                                }
                            )
                        }
                    }

                } else {

                    OutlinedTextField(
                        value = editableUsername,
                        onValueChange = { editableUsername = it },
                        singleLine = true,
                        label = { Text("Your name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        ),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                val trimmed = editableUsername.trim()
                                if (trimmed.isBlank()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Name cannot be empty",
                                            withDismissAction = true
                                        )
                                    }
                                } else {
                                    userViewModel.updateUsername(trimmed)
                                    focusManager.clearFocus()
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Name updated",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            }
                        ) { Text("Change name") }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    NotificationSettingsSection(
                        currentIntervalHours = currentInterval,
                        defaultNotifEnabled = notifDefaultsEnabled,
                        isLandscape = false,
                        notificationsAvailable = notificationsAvailable,
                        onIntervalChange = {
                            userViewModel.updateNotificationInterval(it)
                            if (notificationsAvailable) {
                                scheduleAffirmationWorker(it)
                            }
                        },
                        onDefaultNotifChange = {
                            userViewModel.updateDefaultAffNotif(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Use default affirmations on Home screen",
                                color = Color.White
                            )
                            Text(
                                "If disabled, only your custom affirmations will appear.",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Switch(
                            checked = homeDefaultsEnabled,
                            onCheckedChange = { userViewModel.updateDefaultAffHome(it) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsSection(
    currentIntervalHours: Int,
    defaultNotifEnabled: Boolean,
    isLandscape: Boolean,
    notificationsAvailable: Boolean,
    onIntervalChange: (Int) -> Unit,
    onDefaultNotifChange: (Boolean) -> Unit
) {
    val options = listOf(6, 12, 12, 24, 48, 72, 96)
    var expanded by remember { mutableStateOf(false) }

    var selectedInterval by remember {
        mutableIntStateOf(currentIntervalHours)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(if (isLandscape) 0.7f else 1f)
            .padding(top = 8.dp)
    ) {

        Text(
            "Notification settings",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 12.dp))

        if (!isLandscape) {
            Text(
                "How often should affirmations be sent?",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (notificationsAvailable) {
                    expanded = !expanded
                }
            }
        ) {
            OutlinedTextField(
                value = "$selectedInterval hours",
                onValueChange = {},
                readOnly = true,
                enabled = notificationsAvailable,
                label = { Text("Frequency", color = if (notificationsAvailable) Color.White else Color.Gray) },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = notificationsAvailable
                    )
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.Gray,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    disabledBorderColor = Color.Gray,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    disabledLabelColor = Color.Gray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { hours ->
                    DropdownMenuItem(
                        text = { Text("$hours hours", color = Color.Black) },
                        onClick = {
                            selectedInterval = hours
                            expanded = false
                            onIntervalChange(hours)
                        }
                    )
                }
            }
        }

        if (!notificationsAvailable) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Notifications must be enabled in system settings to schedule affirmations.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Use default affirmations in notifications",
                    color = Color.White
                )
                if (!isLandscape) {
                    Text(
                        "If disabled, only your custom affirmations will be used.",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Switch(
                checked = defaultNotifEnabled,
                onCheckedChange = { onDefaultNotifChange(it) }
            )
        }
    }
}
