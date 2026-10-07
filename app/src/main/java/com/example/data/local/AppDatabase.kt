package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        WrongQuestionEntity::class,
        UnlockRecordEntity::class,
        AnswerLogEntity::class,
        LearningPlanEntity::class,
        BankQuestionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wrongQuestionDao(): WrongQuestionDao
    abstract fun unlockRecordDao(): UnlockRecordDao
    abstract fun learningPathDao(): LearningPathDao
    abstract fun bankQuestionDao(): BankQuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "primary_study_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
