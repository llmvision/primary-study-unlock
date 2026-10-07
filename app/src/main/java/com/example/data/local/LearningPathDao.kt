package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class SubjectStatResult(
    val subjectName: String,
    val totalCount: Int,
    val correctCount: Int
)

@Dao
interface LearningPathDao {
    @Insert
    suspend fun insertAnswerLog(log: AnswerLogEntity)

    @Query("SELECT * FROM answer_logs ORDER BY timestamp DESC")
    fun getAllAnswerLogs(): Flow<List<AnswerLogEntity>>

    @Query("SELECT * FROM answer_logs WHERE gradeLevel = :gradeLevel ORDER BY timestamp DESC")
    fun getLogsByGrade(gradeLevel: Int): Flow<List<AnswerLogEntity>>

    @Query("""
        SELECT subjectName, 
               COUNT(*) as totalCount, 
               SUM(CASE WHEN isCorrect = 1 THEN 1 ELSE 0 END) as correctCount 
        FROM answer_logs 
        GROUP BY subjectName
    """)
    fun getSubjectStats(): Flow<List<SubjectStatResult>>

    @Query("""
        SELECT subjectName, 
               COUNT(*) as totalCount, 
               SUM(CASE WHEN isCorrect = 1 THEN 1 ELSE 0 END) as correctCount 
        FROM answer_logs 
        WHERE gradeLevel = :gradeLevel
        GROUP BY subjectName
    """)
    fun getSubjectStatsByGrade(gradeLevel: Int): Flow<List<SubjectStatResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLearningPlan(plan: LearningPlanEntity)

    @Query("SELECT * FROM learning_plan WHERE planId = :planId LIMIT 1")
    fun getLearningPlanFlow(planId: String = "current_plan"): Flow<LearningPlanEntity?>

    @Query("SELECT * FROM learning_plan WHERE planId = :planId LIMIT 1")
    suspend fun getLearningPlan(planId: String = "current_plan"): LearningPlanEntity?

    @Query("SELECT COUNT(*) FROM answer_logs")
    fun getTotalAnsweredCount(): Flow<Int>
}
