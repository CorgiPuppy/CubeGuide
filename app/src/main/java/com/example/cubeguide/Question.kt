package com.example.cubeguide

data class Question(
    val text: String,
    val type: QuestionType,
    val answers: List<String>,
    val correctAnswers: List<String>,
    val imageResId: Int? = null
)

enum class QuestionType {
    CHECKBOX, FREE_INPUT
}