package com.tbc_final.api_movies.quiz.service

import com.tbc_final.api_movies.quiz.entity.*
import com.tbc_final.api_movies.quiz.repository.AnswerOptionRepository
import com.tbc_final.api_movies.quiz.repository.QuizCategoryRepository
import com.tbc_final.api_movies.quiz.repository.QuizQuestionRepository
import com.tbc_final.api_movies.quiz.repository.QuizUserRepository
import com.tbc_final.api_movies.quiz.util.*
import org.springframework.stereotype.Service

@Service
class QuizService(
    private val categoryRepository: QuizCategoryRepository,
    private val questionRepository: QuizQuestionRepository,
    private val optionRepository: AnswerOptionRepository,
    private val quizUserRepository: QuizUserRepository
) {
    // Get all quiz categories (quizzes)
    fun getAllCategories(): List<QuizCategory> {
        return categoryRepository.findAll().map { it.toModel() }
    }

    // Get a specific quiz category by ID
    fun getCategoryById(id: Int): QuizCategory? {
        return categoryRepository.findById(id).orElse(null)?.toModel()
    }

    // Get all questions for a specific quiz
    fun getQuestionsForCategory(categoryId: Int): List<QuizQuestion> {
        return questionRepository.findByCategoryId(categoryId).map { questionEntity ->
            val options = optionRepository.findByQuestionId(questionEntity.id)
            questionEntity.toModel(options.map { it.toModel() })
        }
    }

    // Get all quizzes with their status for a user
    fun getUserQuizzes(userUid: String): List<QuizUserStatus> {
        // Get user's existing quiz statuses
        val userQuizStatuses = quizUserRepository.findByIdUserUid(userUid).associateBy { it.id.quizId }

        // Get all available quizzes
        val allQuizzes = categoryRepository.findAll()

        // Return all quizzes with their status (or NOT_COMPLETED if not started)
        return allQuizzes.map { quizCategory ->
            val userQuiz = userQuizStatuses[quizCategory.id]
            if (userQuiz != null) {
                QuizUserStatus(
                    userUid = userUid,
                    quizId = quizCategory.id,
                    status = userQuiz.status,
                    completionDate = userQuiz.completionDate,
                    score = userQuiz.score
                )
            } else {
                QuizUserStatus(
                    userUid = userUid,
                    quizId = quizCategory.id,
                    status = QuizStatus.NOT_COMPLETED.name,
                    completionDate = null,
                    score = null
                )
            }
        }
    }

    // Get all completed quizzes for a user
    fun getUserCompletedQuizzes(userUid: String): List<QuizUserStatus> {
        return quizUserRepository.findByIdUserUidAndStatus(userUid, QuizStatus.COMPLETED.name).map { it.toModel() }
    }

    // Save or update quiz status for a user
    fun saveUserQuizStatus(userQuizStatus: QuizUserStatus): QuizUserStatus {
        val entity = QuizUserEntity(
            id = QuizUserId(userQuizStatus.userUid, userQuizStatus.quizId),
            status = userQuizStatus.status,
            completionDate = userQuizStatus.completionDate,
            score = userQuizStatus.score
        )
        return quizUserRepository.save(entity).toModel()
    }

    // Extension functions to convert between entities and models (same as before)
    private fun QuizCategoryEntity.toModel(): QuizCategory {
        return QuizCategory(
            id = this.id,
            title = this.title,
            imageUrl = this.imageUrl,
            questionCount = this.questionCount,
            rewardCoins = this.rewardCoins,
            difficulty = this.difficulty
        )
    }

    private fun QuizQuestionEntity.toModel(options: List<AnswerOption>): QuizQuestion {
        return QuizQuestion(
            id = this.id,
            question = this.question,
            imageUrl = this.imageUrl,
            options = options,
            correctOptionId = this.correctOptionId
        )
    }

    private fun AnswerOptionEntity.toModel(): AnswerOption {
        return AnswerOption(
            id = this.id,
            letter = this.letter,
            text = this.text
        )
    }

    private fun QuizUserEntity.toModel(): QuizUserStatus {
        return QuizUserStatus(
            userUid = this.id.userUid,
            quizId = this.id.quizId,
            status = this.status,
            completionDate = this.completionDate,
            score = this.score
        )
    }
}