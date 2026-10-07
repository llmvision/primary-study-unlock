package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_questions")
data class BankQuestionEntity(
    @PrimaryKey
    val id: String,
    val gradeLevel: Int, // 4, 5, 6
    val subjectType: String, // CHINESE_ESSAY, MATH, ENGLISH
    val title: String,
    val optionsJson: String,
    val correctIndex: Int,
    val explanation: String,
    val sampleEssence: String = "",
    val keyPointsJson: String = "",
    val isCustomOrRemote: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
