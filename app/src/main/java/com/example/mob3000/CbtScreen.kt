package com.example.mob3000

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mob3000.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CbtScreen(
    onGoHome: () -> Unit,
    onStartSession: () -> Unit
) {
    val orientation = LocalConfiguration.current.orientation

    GradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cognitive Behavioral Therapy", fontSize = 20.sp) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
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
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = onGoHome,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .height(50.dp)
                                ) {
                                    Text("Home")
                                }

                                Button(
                                    onClick = onStartSession,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .height(50.dp)
                                ) {
                                    Text("Start New Session")
                                }
                            }
                        }
                    }
                }
            },
            containerColor = Color.Transparent
        ) { innerPadding ->

            val baseModifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize()

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                CbtPortraitContent(
                    modifier = baseModifier,
                    onStartSession = onStartSession
                )
            } else {
                CbtLandscapeContent(
                    modifier = baseModifier
                )
            }
        }
    }
}

@Composable
private fun CbtDescription(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cognitive behavioral therapy is an evidence-based treatment that focuses on identifying and challenging unhelpful thoughts and replacing them with more accurate, balanced ones. It also targets the behaviors that maintain anxiety or distress, helping you build healthier patterns. The goal is to change how you think and act so your emotional responses improve.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "You will be guided through six CBT questions about your current situation, and once completed, you will have the opportunity to reflect on your answers to gain further insight.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CbtPortraitContent(
    modifier: Modifier = Modifier,
    onStartSession: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CbtDescription()

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onStartSession,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start New Session")
        }
    }
}

@Composable
private fun CbtLandscapeContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CbtDescription(
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}
