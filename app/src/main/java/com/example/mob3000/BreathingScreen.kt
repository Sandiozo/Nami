package com.example.mob3000

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.animation.core.tween as coreTween
import com.example.mob3000.ui.theme.GradientBackground

// Enum for each breathing phase with the display label
enum class BreathPhase(val label: String) {
    Inhale("Inhale"),
    Hold("Hold"),
    Exhale("Exhale")
}

@Composable
fun BreathingScreen(
    onGoHome: () -> Unit = {}
) {
    // List of the breathing phases and the duration in milliseconds
    val phases = listOf(
        BreathPhase.Inhale to 4000L,
        BreathPhase.Hold to 7000L,
        BreathPhase.Exhale to 8000L
    )

    var phaseIndex by remember { mutableIntStateOf(0) }
    var currentPhase by remember { mutableStateOf(phases[0].first) }

    val scale = remember { Animatable(0.6f) }
    var started by remember { mutableStateOf(false) }

    val breakAfterExhale = 2000L

    // Breathing animation per phase
    if (started) {
        LaunchedEffect(phaseIndex) {
            val (phase, duration) = phases[phaseIndex]
            currentPhase = phase

            when (phase) {
                BreathPhase.Inhale -> {
                    scale.animateTo(
                        targetValue = 1.2f,
                        animationSpec = coreTween(durationMillis = duration.toInt())
                    )
                }
                BreathPhase.Hold -> delay(duration)
                BreathPhase.Exhale -> {
                    scale.animateTo(
                        targetValue = 0.6f,
                        animationSpec = coreTween(durationMillis = duration.toInt())
                    )
                    delay(breakAfterExhale)
                }
            }

            if (started) {
                phaseIndex = (phaseIndex + 1) % phases.size
            }
        }
    }
    GradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    tonalElevation = 0.dp
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
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(300.dp * scale.value)
                        .clip(CircleShape)
                        .background(Color(0xff6a4c93))
                        .clickable {
                            if (!started) {
                                phaseIndex = 0
                            }
                            started = !started
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedContent(
                            targetState = if (!started) "Tap to start" else currentPhase.label,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith
                                        fadeOut(animationSpec = tween(300))
                            },
                            label = "breathing_text"
                        ) { text ->
                            Text(
                                text = text,
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BreathingScreenPreview() {
    BreathingScreen()
}