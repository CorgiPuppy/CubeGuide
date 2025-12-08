package com.example.cubeguide

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    private val questions = listOf(
        Question(
            text = "Какие из этих головоломок имеют форму куба?",
            type = QuestionType.CHECKBOX,
            answers = listOf("Megaminx", "2x2", "Mirror cube", "Pyraminx"),
            correctAnswers = listOf("2x2", "Mirror cube")
        ),

        Question(
            text = "Какие методы используются для скоростной сборки?",
            type = QuestionType.CHECKBOX,
            answers = listOf("CFOP", "Roux", "Послойный", "Интуитивный"),
            correctAnswers = listOf("CFOP", "Roux")
        ),

        Question(
            text = "Сколько граней у классического кубика Рубика? (число)",
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf("6", "шесть")
        ),

        Question(
            text = "Как называется эта головоломка?",
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf("megaminx", "мегаминкс")
        ),

        Question(
            text = "Как называется у этой головоломка? (число)",
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf("megaminx", "мегаминкс"),
            imageResId = R.drawable.four_on_four
        )
    )

    var currentIndex = 0
    var score = 0
    var isGameFinished = false

    fun getCurrentQuestion(): Question {
        return questions[currentIndex]
    }

    fun getTotalQuestionsCount(): Int = questions.size

    fun checkAnswer(userAnswers: List<String>) {
        val currentQ = questions[currentIndex]

        val normalizedUserAnswers = userAnswers.map { it.trim().lowercase() }.sorted()
        val normalizedCorrectAnswers = currentQ.correctAnswers.map { it.trim().lowercase() }.sorted()

        if (currentQ.type == QuestionType.CHECKBOX) {
            if (normalizedUserAnswers == normalizedCorrectAnswers)
                score++
        } else
            if (userAnswers.isNotEmpty() && normalizedCorrectAnswers.contains(normalizedUserAnswers[0]))
                score++
    }

    fun moveToNext() {
        if (currentIndex < questions.size - 1)
            currentIndex++
        else
            isGameFinished = true
    }
}