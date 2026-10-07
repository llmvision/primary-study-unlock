package com.example.data.model

data class DiagnosticResult(
    val grade: Grade,
    val totalAnswered: Int,
    val overallAccuracy: Float, // 0.0 to 1.0
    val subjectAccuracyMap: Map<Subject, Float>,
    val subjectAnsweredMap: Map<Subject, Int>,
    val weakSubject: Subject?,
    val strongSubject: Subject?,
    val masteryLevel: String, // 基础稳固 / 进阶强化 / 冲刺飞跃
    val advice: String,
    val recommendedTopics: List<String>
)

data class RecommendedPractice(
    val title: String,
    val subject: Subject,
    val difficulty: String,
    val reason: String,
    val questions: List<Question>
)
