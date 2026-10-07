package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer_logs")
data class AnswerLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val questionId: String,
    val gradeLevel: Int,
    val subjectName: String,
    val isCorrect: Boolean,
    val selectedIndex: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "learning_plan")
data class LearningPlanEntity(
    @PrimaryKey
    val planId: String = "current_plan",
    val gradeLevel: Int,
    val currentLevelName: String, // 基础稳固 / 进阶强化 / 拔尖冲刺
    val weakSubject: String,
    val strongSubject: String,
    val dailyTargetQuestions: Int = 5,
    val recommendedTopic: String,
    val studyAdvice: String,
    val updatedAt: Long = System.currentTimeMillis()
)
