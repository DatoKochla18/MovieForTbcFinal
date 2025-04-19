package com.tbc_final.api_movies.quiz.repository

import com.tbc_final.api_movies.quiz.entity.QuizQuestionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizQuestionRepository : JpaRepository<QuizQuestionEntity, Int> {
    fun findByCategoryId(categoryId: Int): List<QuizQuestionEntity>
}