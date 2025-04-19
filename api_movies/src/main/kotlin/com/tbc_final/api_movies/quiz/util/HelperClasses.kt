package com.tbc_final.api_movies.quiz.util

import java.time.LocalDateTime

data class AnswerOption(
    val id: String,
    val letter: String,
    val text: String
)

data class QuizCategory(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val questionCount: Int,
    val rewardCoins: Int,
    val difficulty: String
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val imageUrl: String,
    val options: List<AnswerOption>,
    val correctOptionId: String
)

// Adding a new data class for the quiz user status
data class QuizUserStatus(
    val userUid: String,
    val quizId: Int,
    val status: String,
    val completionDate: LocalDateTime? = null,
    val score: Int? = null
)

// Enum for quiz status
enum class QuizStatus {
    COMPLETED,
    NOT_COMPLETED,
    IN_PROGRESS
}