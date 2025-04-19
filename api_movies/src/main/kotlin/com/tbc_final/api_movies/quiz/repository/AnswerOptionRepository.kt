package com.tbc_final.api_movies.quiz.repository

import com.tbc_final.api_movies.quiz.entity.AnswerOptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnswerOptionRepository : JpaRepository<AnswerOptionEntity, String> {
    fun findByQuestionId(questionId: Int): List<AnswerOptionEntity>
}
