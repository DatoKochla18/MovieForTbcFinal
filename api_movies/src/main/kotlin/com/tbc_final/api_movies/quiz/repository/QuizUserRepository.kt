package com.tbc_final.api_movies.quiz.repository

import com.tbc_final.api_movies.quiz.entity.QuizUserEntity
import com.tbc_final.api_movies.quiz.entity.QuizUserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizUserRepository : JpaRepository<QuizUserEntity, QuizUserId> {
    fun findByIdUserUid(userUid: String): List<QuizUserEntity>
    fun findByIdUserUidAndStatus(userUid: String, status: String): List<QuizUserEntity>
}
