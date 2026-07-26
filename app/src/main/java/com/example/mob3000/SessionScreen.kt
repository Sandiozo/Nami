package com.example.mob3000

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mob3000.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    onGoBackToCBT: () -> Unit,
    viewModel: SessionViewModel = viewModel()
) {
    val questions = viewModel.questions
    val currentIndex = viewModel.currentIndex
    val currentAnswer = viewModel.currentAnswer
    val showValidationError = viewModel.showValidationError
    val sessionFinished = viewModel.sessionFinished
    val answers = viewModel.answers

    val focusManager = LocalFocusManager.current

    GradientBackground {

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                if (sessionFinished) {
                    BottomAppBar(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    viewModel.reset()
                                    onGoBackToCBT()
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(50.dp)
                            ) {
                                Text("Finish Session")
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { focusManager.clearFocus() }
                    },
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                if (!sessionFinished) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of ${questions.size}",
                            fontSize = 18.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = questions[currentIndex],
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = currentAnswer,
                            onValueChange = { newText ->
                                if (newText.length <= 1000) {
                                    viewModel.onAnswerChange(newText)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            maxLines = 5,
                            label = { Text("Your answer", color = Color.White) },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.LightGray,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent
                            )
                        )

                        if (showValidationError) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Answer cannot be empty.",
                                color = Color(0xFFFF6F6F),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { viewModel.goPrevious() },
                            enabled = currentIndex > 0,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                disabledContentColor = Color(0xFF555555)
                            )
                        ) {
                            Text("Previous")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { viewModel.goNext() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                if (currentIndex == questions.lastIndex) "Summarize"
                                else "Next"
                            )
                        }
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Session summary",
                                fontSize = 24.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        itemsIndexed(questions) { index, question ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = question,
                                    fontSize = 14.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color.White
                                )

                                Text(
                                    text = answers[index],
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}
