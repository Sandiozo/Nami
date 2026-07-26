package com.example.mob3000

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.database.AffirmationViewModel
import com.example.mob3000.notifications.NotificationHelper
import com.example.mob3000.ui.theme.GradientBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    onOpenSettings: () -> Unit,
    onGoToAffirmations: () -> Unit,
    onGoToBreathing: () -> Unit,
    onGoToCBT: () -> Unit,
    homeDefaultAffirmationsEnabled: Boolean
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val affVm = remember { AffirmationViewModel(context.applicationContext) }

    val customAffs = affVm.customAffirmations

    val affirmationText =
        if (homeDefaultAffirmationsEnabled) {
            affVm.randomAffirmation ?: "No affirmations yet."
        } else {
            if (customAffs.isNotEmpty()) {
                customAffs.random().text
            } else {
                "No custom affirmations yet."
            }
        }

    GradientBackground {
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            PortraitLayout(
                username = username,
                onOpenSettings = onOpenSettings,
                onGoToAffirmations = onGoToAffirmations,
                onGoToCBT = onGoToCBT,
                onGoToBreathing = onGoToBreathing,
                affirmationText = affirmationText,
                affVm = affVm
            )
        } else {
            LandscapeLayout(
                username = username,
                onOpenSettings = onOpenSettings,
                onGoToAffirmations = onGoToAffirmations,
                onGoToCBT = onGoToCBT,
                onGoToBreathing = onGoToBreathing,
                affirmationText = affirmationText,
                affVm = affVm
            )
        }
    }
}

@Composable
private fun HomeButton(text: String, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(70.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitLayout(
    username: String,
    onOpenSettings: () -> Unit,
    onGoToAffirmations: () -> Unit,
    onGoToCBT: () -> Unit,
    onGoToBreathing: () -> Unit,
    affirmationText: String,
    affVm: AffirmationViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    Text(
                        "Hello, $username",
                        modifier = Modifier.padding(end = 16.dp),
                        textAlign = TextAlign.End
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    affirmationText,
                    fontSize = 32.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )
            }

            HomeButton("Cognitive Behavioral Therapy", onGoToCBT)
            HomeButton("Breathing", onGoToBreathing)
            HomeButton("Affirmations", onGoToAffirmations)

            // Test notification button
            HomeButton("Send Test Notification") {
                scope.launch {
                    val text = affVm.randomAffirmation
                        ?: "No affirmations yet. Add some custom ones!"
                    NotificationHelper.showAffirmationNotification(context, text)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeLayout(
    username: String,
    onOpenSettings: () -> Unit,
    onGoToAffirmations: () -> Unit,
    onGoToCBT: () -> Unit,
    onGoToBreathing: () -> Unit,
    affirmationText: String,
    affVm: AffirmationViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    Text(
                        "Welcome, $username",
                        modifier = Modifier.padding(end = 16.dp),
                        textAlign = TextAlign.End
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    affirmationText,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HomeButton("Cognitive Behavioral Therapy", onGoToCBT)
                HomeButton("Breathing", onGoToBreathing)
                HomeButton("Affirmations", onGoToAffirmations)

                // This button as added only for testing purposes
                HomeButton("Send Test Notification") {
                    scope.launch {
                        val text = affVm.randomAffirmation
                            ?: "No affirmations yet. Add some custom ones!"
                        NotificationHelper.showAffirmationNotification(context, text)
                    }
                }
            }
        }
    }
}
