package com.tbc_final.api_movies.quiz.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "quiz_user")
data class QuizUserEntity(
    @EmbeddedId
    val id: QuizUserId,
    val status: String,
    val completionDate: LocalDateTime? = null,
    val score: Int? = null
)

@Embeddable
data class QuizUserId(
    val userUid: String,
    val quizId: Int
) : Serializable
