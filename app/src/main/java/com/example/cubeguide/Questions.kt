package com.example.cubeguide

data class Question(
    val id: Int,
    val text: String,
    val imageRes: Int? = null,
    val correctAnswer: String,
    val type: String
)
