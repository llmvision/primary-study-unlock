package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WrongQuestionDao {
    @Query("SELECT * FROM wrong_questions WHERE isMastered = 0 ORDER BY lastAttemptTimestamp DESC")
    fun getActiveWrongQuestions(): Flow<List<WrongQuestionEntity>>

    @Query("SELECT * FROM wrong_questions ORDER BY lastAttemptTimestamp DESC")
    fun getAllWrongQuestions(): Flow<List<WrongQuestionEntity>>

    @Query("SELECT * FROM wrong_questions WHERE questionId = :questionId LIMIT 1")
    suspend fun getWrongQuestionById(questionId: String): WrongQuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWrongQuestion(question: WrongQuestionEntity)

    @Query("UPDATE wrong_questions SET isMastered = 1 WHERE questionId = :questionId")
    suspend fun markAsMastered(questionId: String)

    @Query("DELETE FROM wrong_questions WHERE questionId = :questionId")
    suspend fun deleteWrongQuestion(questionId: String)

    @Query("SELECT COUNT(*) FROM wrong_questions WHERE isMastered = 0")
    fun getUnmasteredCount(): Flow<Int>
}

@Dao
interface UnlockRecordDao {
    @Query("SELECT * FROM unlock_records ORDER BY timestamp DESC LIMIT 30")
    fun getRecentUnlockRecords(): Flow<List<UnlockRecordEntity>>

    @Insert
    suspend fun insertRecord(record: UnlockRecordEntity)

    @Query("SELECT COUNT(*) FROM unlock_records WHERE isSuccess = 1")
    fun getSuccessUnlockCount(): Flow<Int>
}
