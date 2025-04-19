package com.tbc_final.api_movies.quiz.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "quiz_category")
data class QuizCategoryEntity(
    @Id
    val id: Int,
    val title: String,
    val imageUrl: String,
    val questionCount: Int,
    val rewardCoins: Int,
    val difficulty: String
)

