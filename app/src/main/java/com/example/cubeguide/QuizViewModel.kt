package com.example.cubeguide

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private fun getString(resId: Int): String {
        return getApplication<Application>().resources.getString(resId)
    }

    private val questions = listOf(
        Question(
            text = getString(R.string.q1_text),
            type = QuestionType.CHECKBOX,
            answers = listOf(
                getString(R.string.q1_a1),
                getString(R.string.q1_a2),
                getString(R.string.q1_a3),
                getString(R.string.q1_a4),
            ),
            correctAnswers = listOf(
                getString(R.string.q1_a2),
                getString(R.string.q1_a3)
            )
        ),

        Question(
            text = getString(R.string.q2_text),
            type = QuestionType.CHECKBOX,
            answers = listOf(
                getString(R.string.q2_a1),
                getString(R.string.q2_a2),
                getString(R.string.q2_a3),
                getString(R.string.q2_a4),
            ),
            correctAnswers = listOf(
                getString(R.string.q2_a1),
                getString(R.string.q2_a2)
            )
        ),

        Question(
            text = getString(R.string.q3_text),
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf(
                getString(R.string.q3_a1),
                getString(R.string.q3_a2),
                getString(R.string.q3_a3),
            )
        ),

        Question(
            text = getString(R.string.q4_text),
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf(
                getString(R.string.q4_a1),
                getString(R.string.q4_a2),
            ),
        ),

        Question(
            text = getString(R.string.q5_text),
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf(
                getString(R.string.q5_a1),
                getString(R.string.q5_a2),
                getString(R.string.q5_a3),
                getString(R.string.q5_a4),
                getString(R.string.q5_a5),
                getString(R.string.q5_a6),
                getString(R.string.q5_a7),
            ),
            imageResId = R.drawable.four_on_four
        ),

        Question(
            text = getString(R.string.q6_text),
            type = QuestionType.FREE_INPUT,
            answers = emptyList(),
            correctAnswers = listOf(
                getString(R.string.q6_a1),
                getString(R.string.q6_a2),
            ),
            imageResId = R.drawable.megaminx
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
            if (userAnswers.isNotEmpty()) {
                val userAnswer = userAnswers[0].trim().lowercase()
                if (normalizedCorrectAnswers.contains(userAnswer))
                    score++
            }
    }

    fun moveToNext() {
        if (currentIndex < questions.size - 1)
            currentIndex++
        else
            isGameFinished = true
    }
}