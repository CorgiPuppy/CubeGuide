package com.example.cubeguide

data class Question(
    val id: Int,
    val text: String,
    val imageRes: Int? = null,
    val correctAnswer: String,
    val type: String
)

object Questions {
    val questions = listOf(
        Question(1, "Как называется данная головоломка?", R.drawable.megaminx, "Мегаминкс", "image"),
        Question(2, "Что изображено на рисунке?", R.drawable.mirror, "Зеркальный куб", "image"),

        Question(3, "Сколько граней у классического кубика Рубика?", null, "6", "text"),
        Question(4, "Как называется самый популярный метод сборки?", null, "Метод Фридрих", "text"),

        Question(5, "Назовите виды головоломок", null, "Кубик Рубика, Мегаминкс, Зеркальный куб", "text"),
        Question(6, "Основные этапы сборки", null, "Крест, Углы, Второй слой, ОЛЛ, ПЛЛ", "text")
    )
}
