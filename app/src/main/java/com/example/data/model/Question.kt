package com.example.data.model

enum class Grade(val level: Int, val label: String) {
    GRADE_4(4, "四年级"),
    GRADE_5(5, "五年级"),
    GRADE_6(6, "六年级")
}

enum class Subject(val label: String, val iconName: String) {
    CHINESE_ESSAY("语文与作文", "chinese"),
    MATH("数学与逻辑", "math"),
    ENGLISH("小学核心英语", "english")
}

data class Question(
    val id: String,
    val grade: Grade,
    val subject: Subject,
    val title: String,
    val contextInfo: String = "",
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val keyPoints: List<String> = emptyList(),
    val sampleEssence: String = "" // For essay questions: good sentence, expression technique, or math formula
)
