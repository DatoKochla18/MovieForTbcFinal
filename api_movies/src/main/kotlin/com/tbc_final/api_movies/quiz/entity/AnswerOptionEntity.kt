package com.tbc_final.api_movies.quiz.entity

import jakarta.persistence.*

@Entity
@Table(name = "answer_option")
data class AnswerOptionEntity(
    @Id
    val id: String,
    val letter: String,
    val text: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    val question: QuizQuestionEntity
)