package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wrong_questions")
data class WrongQuestionEntity(
    @PrimaryKey
    val questionId: String,
    val gradeLevel: Int,
    val subjectName: String,
    val title: String,
    val optionsJson: String, // comma or pipe separated options
    val correctIndex: Int,
    val userSelectedIndex: Int,
    val explanation: String,
    val sampleEssence: String,
    val wrongCount: Int = 1,
    val lastAttemptTimestamp: Long = System.currentTimeMillis(),
    val isMastered: Boolean = false
)

@Entity(tableName = "unlock_records")
data class UnlockRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val totalQuestions: Int,
    val correctQuestions: Int,
    val gradeLevel: Int,
    val isSuccess: Boolean
)
