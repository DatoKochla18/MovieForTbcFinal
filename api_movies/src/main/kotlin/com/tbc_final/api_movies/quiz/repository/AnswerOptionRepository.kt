package com.tbc_final.api_movies.quiz.repository

import com.tbc_final.api_movies.quiz.entity.AnswerOptionEntity
import com.tbc_final.api_movies.quiz.entity.AnswerOptionId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnswerOptionRepository
    : JpaRepository<AnswerOptionEntity, AnswerOptionId> {
    fun findByIdQuestionId(questionId: Int): List<AnswerOptionEntity>

}