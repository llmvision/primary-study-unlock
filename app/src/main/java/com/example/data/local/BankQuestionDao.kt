package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BankQuestionDao {
    @Query("SELECT * FROM bank_questions ORDER BY gradeLevel ASC, subjectType ASC")
    fun getAllQuestionsFlow(): Flow<List<BankQuestionEntity>>

    @Query("SELECT * FROM bank_questions WHERE gradeLevel = :gradeLevel")
    fun getQuestionsByGradeFlow(gradeLevel: Int): Flow<List<BankQuestionEntity>>

    @Query("SELECT * FROM bank_questions WHERE gradeLevel = :gradeLevel")
    suspend fun getQuestionsByGrade(gradeLevel: Int): List<BankQuestionEntity>

    @Query("SELECT * FROM bank_questions")
    suspend fun getAllQuestions(): List<BankQuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<BankQuestionEntity>)

    @Query("SELECT COUNT(*) FROM bank_questions")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM bank_questions")
    fun getCountFlow(): Flow<Int>
}
