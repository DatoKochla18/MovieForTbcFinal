package com.tbc_final.api_movies.quiz.controller

import com.tbc_final.api_movies.quiz.service.QuizService
import com.tbc_final.api_movies.quiz.util.QuizCategory
import com.tbc_final.api_movies.quiz.util.QuizQuestion
import com.tbc_final.api_movies.quiz.util.QuizStatus
import com.tbc_final.api_movies.quiz.util.QuizUserStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/quiz")
class QuizController(private val quizService: QuizService) {

    // Get all quizzes (categories)
    @GetMapping("/quizzes")
    fun getAllQuizzes(): ResponseEntity<List<QuizCategory>> {
        return ResponseEntity.ok(quizService.getAllCategories())
    }

    // Get quiz by id
    @GetMapping("/quizzes/{id}")
    fun getQuizById(@PathVariable id: Int): ResponseEntity<QuizCategory> {
        val category = quizService.getCategoryById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(category)
    }

    // Get all questions for a specific quiz
    @GetMapping("/quizzes/{quizId}/questions")
    fun getQuestionsByQuiz(@PathVariable quizId: Int): ResponseEntity<List<QuizQuestion>> {
        return ResponseEntity.ok(quizService.getQuestionsForCategory(quizId))
    }

    // Get all completed quizzes for a user
    @GetMapping("/users/{userUid}/quizzes/completed")
    fun getCompletedQuizzes(@PathVariable userUid: String): ResponseEntity<List<QuizUserStatus>> {
        return ResponseEntity.ok(quizService.getUserCompletedQuizzes(userUid))
    }

    // Get all quizzes with their status for a user
    @GetMapping("/users/{userUid}/quizzes")
    fun getAllUserQuizzes(@PathVariable userUid: String): ResponseEntity<List<QuizUserStatus>> {
        return ResponseEntity.ok(quizService.getUserQuizzes(userUid))
    }

    // Update quiz status (to COMPLETED or NOT_COMPLETED)
    @PutMapping("/users/{userUid}/quizzes/{quizId}/status")
    fun updateQuizStatus(
        @PathVariable userUid: String,
        @PathVariable quizId: Int,
        @RequestBody request: QuizStatusUpdateRequest
    ): ResponseEntity<QuizUserStatus> {
        val quizUserStatus = QuizUserStatus(
            userUid = userUid,
            quizId = quizId,
            status = request.status,
            completionDate = if (request.status == QuizStatus.COMPLETED.name) LocalDateTime.now() else null,
            score = request.score
        )
        return ResponseEntity.ok(quizService.saveUserQuizStatus(quizUserStatus))
    }

    @PostMapping("/users/{userUid}/quizzes/{quizId}/complete")
    fun completeQuiz(
        @PathVariable userUid: String,
        @PathVariable quizId: Int
    ): ResponseEntity<QuizUserStatus> {
        val quizUserStatus = QuizUserStatus(
            userUid = userUid,
            quizId = quizId,
            status = QuizStatus.COMPLETED.name,
            completionDate = LocalDateTime.now(),
            score = null  // No score needed
        )
        return ResponseEntity.ok(quizService.saveUserQuizStatus(quizUserStatus))
    }
}

data class QuizStatusUpdateRequest(
    val status: String, // Should be either "COMPLETED" or "NOT_COMPLETED"
    val score: Int? = null
)