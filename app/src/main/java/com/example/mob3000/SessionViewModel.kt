package com.example.mob3000

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SessionViewModel : ViewModel() {
    // List of all the question texts
    val questions = listOf(
        "What situation are you reflecting on?",
        "What thoughts went through your mind?",
        "What emotions did you feel (and how intense were they 1–10)?",
        "What did you do or how did you react?",
        "What evidence supports your thoughts?",
        "What evidence goes against your thoughts?"
    )
    // Checks at which question the user is
    var currentIndex by mutableStateOf(0)
    // Checks for what is being currently typed
    var currentAnswer by mutableStateOf("")
    // If the answer is empty will set to true making it imposible to skip the question.
    var showValidationError by mutableStateOf(false)

    // Stores all the answers
    val answers = mutableStateListOf<String>().apply {
        addAll(List(questions.size) { "" })
    }
    // Checks if the session is done with the help of index
    val sessionFinished: Boolean
        get() = currentIndex >= questions.size
    // remove error message when text field has been filled
    fun onAnswerChange(newValue: String) {
        currentAnswer = newValue
        if (showValidationError && newValue.trim().isNotEmpty()) {
            showValidationError = false
        }
    }

    fun goNext() {
        val trimmed = currentAnswer.trim()

        if (trimmed.isEmpty()) {
            showValidationError = true
            return
        }

    // Save current answer
        if (currentIndex in answers.indices) {
            answers[currentIndex] = trimmed
        }

    // Move to the next question
        if (currentIndex < questions.lastIndex) {
            currentIndex += 1
            currentAnswer = answers[currentIndex]
        } else {
    // Go to the summary
            currentIndex += 1
            currentAnswer = ""
        }

        showValidationError = false
    }

    fun goPrevious() {
        if (currentIndex <= 0) return

    // Save the current answer before going back
        val trimmed = currentAnswer.trim()
        if (currentIndex in answers.indices) {
            answers[currentIndex] = trimmed
        }

        currentIndex -= 1
        currentAnswer = answers[currentIndex]
        showValidationError = false
    }
    // resets the session
    fun reset() {
        currentIndex = 0
        currentAnswer = ""
        showValidationError = false
        answers.clear()
        answers.addAll(List(questions.size) { "" })
    }
}
