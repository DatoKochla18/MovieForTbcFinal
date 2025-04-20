package com.tbc_final.api_movies.quiz.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "answer_option")
data class AnswerOptionEntity(
    @EmbeddedId
    val id: AnswerOptionId,
    val text: String,
    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    val question: QuizQuestionEntity
)

@Embeddable
data class AnswerOptionId(
    @Column(name = "question_id")
    val questionId: Int,
    @Column(name = "letter")
    val letter: String
) : Serializable