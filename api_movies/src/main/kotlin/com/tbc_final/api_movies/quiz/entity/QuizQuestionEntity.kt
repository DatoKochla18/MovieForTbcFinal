package com.tbc_final.api_movies.quiz.entity

import jakarta.persistence.*

@Entity
@Table(name = "quiz_question")
data class QuizQuestionEntity(
    @Id
    val id: Int,
    val question: String,
    val imageUrl: String,
    val correctOptionId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: QuizCategoryEntity,

    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    val options: MutableList<AnswerOptionEntity> = mutableListOf()
)
