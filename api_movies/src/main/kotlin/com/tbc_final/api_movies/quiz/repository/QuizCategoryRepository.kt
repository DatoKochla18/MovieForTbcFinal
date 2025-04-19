package com.tbc_final.api_movies.quiz.repository

import com.tbc_final.api_movies.quiz.entity.QuizCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface QuizCategoryRepository : JpaRepository<QuizCategoryEntity, Int>