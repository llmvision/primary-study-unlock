package com.example.data.repository

import com.example.data.local.AnswerLogEntity
import com.example.data.local.BankQuestionDao
import com.example.data.local.LearningPathDao
import com.example.data.local.LearningPlanEntity
import com.example.data.local.UnlockRecordDao
import com.example.data.local.UnlockRecordEntity
import com.example.data.local.WrongQuestionDao
import com.example.data.local.WrongQuestionEntity
import com.example.data.model.DiagnosticResult
import com.example.data.model.Grade
import com.example.data.model.Question
import com.example.data.model.RecommendedPractice
import com.example.data.model.Subject
import com.example.data.repository.ExpandedQuestionData.toEntity
import com.example.data.repository.ExpandedQuestionData.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StudyRepository(
    private val wrongQuestionDao: WrongQuestionDao,
    private val unlockRecordDao: UnlockRecordDao,
    private val learningPathDao: LearningPathDao,
    private val bankQuestionDao: BankQuestionDao
) {
    val activeWrongQuestions: Flow<List<WrongQuestionEntity>> =
        wrongQuestionDao.getActiveWrongQuestions()

    val allWrongQuestions: Flow<List<WrongQuestionEntity>> =
        wrongQuestionDao.getAllWrongQuestions()

    val unmasteredCount: Flow<Int> =
        wrongQuestionDao.getUnmasteredCount()

    val recentRecords: Flow<List<UnlockRecordEntity>> =
        unlockRecordDao.getRecentUnlockRecords()

    val totalSuccessfulUnlocks: Flow<Int> =
        unlockRecordDao.getSuccessUnlockCount()

    val totalAnsweredLogsCount: Flow<Int> =
        learningPathDao.getTotalAnsweredCount()

    val currentLearningPlan: Flow<LearningPlanEntity?> =
        learningPathDao.getLearningPlanFlow()

    val totalBankCount: Flow<Int> =
        bankQuestionDao.getCountFlow()

    // Initialize Question Database with pre-bundled questions if empty
    suspend fun initializeQuestionBankIfNeeded() {
        withContext(Dispatchers.IO) {
            val count = bankQuestionDao.getCount()
            if (count == 0) {
                // Populate base questions from QuestionBank
                val entities = QuestionBank.allQuestions.map { it.toEntity(isRemote = false) }
                bankQuestionDao.insertAll(entities)
            }
        }
    }

    // Sync / Update Questions from cloud/online mock service
    suspend fun syncOnlineQuestionBank(): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                // Simulate an online API fetch delay and get latest curriculum questions
                kotlinx.coroutines.delay(1200)

                // Combine extra curated expansion questions + new curriculum sync items
                val latestFromCloud = ExpandedQuestionData.extraCuratedQuestions.map {
                    it.toEntity(isRemote = true)
                }

                bankQuestionDao.insertAll(latestFromCloud)
                val totalCount = bankQuestionDao.getCount()
                Result.success(totalCount)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getQuestionsForGrade(grade: Grade): List<Question> {
        return withContext(Dispatchers.IO) {
            val entities = bankQuestionDao.getQuestionsByGrade(grade.level)
            if (entities.isNotEmpty()) {
                entities.map { it.toModel() }
            } else {
                QuestionBank.getQuestionsByGrade(grade)
            }
        }
    }

    fun getQuestionsForGradeFlow(grade: Grade): Flow<List<Question>> {
        return bankQuestionDao.getQuestionsByGradeFlow(grade.level).map { entities ->
            if (entities.isNotEmpty()) {
                entities.map { it.toModel() }
            } else {
                QuestionBank.getQuestionsByGrade(grade)
            }
        }
    }

    suspend fun getRandomQuiz(grade: Grade, count: Int = 3): List<Question> {
        val gradeQuestions = getQuestionsForGrade(grade)
        val chinese = gradeQuestions.filter { it.subject == Subject.CHINESE_ESSAY }.shuffled()
        val math = gradeQuestions.filter { it.subject == Subject.MATH }.shuffled()
        val english = gradeQuestions.filter { it.subject == Subject.ENGLISH }.shuffled()

        val list = mutableListOf<Question>()
        if (chinese.isNotEmpty()) list.add(chinese.first())
        if (math.isNotEmpty()) list.add(math.first())
        if (english.isNotEmpty()) list.add(english.first())

        val remaining = gradeQuestions.filterNot { list.contains(it) }.shuffled()
        for (q in remaining) {
            if (list.size >= count) break
            list.add(q)
        }
        return list.take(count).shuffled()
    }

    suspend fun recordAnswerResult(question: Question, selectedIndex: Int, isCorrect: Boolean) {
        learningPathDao.insertAnswerLog(
            AnswerLogEntity(
                questionId = question.id,
                gradeLevel = question.grade.level,
                subjectName = question.subject.label,
                isCorrect = isCorrect,
                selectedIndex = selectedIndex
            )
        )

        if (!isCorrect) {
            val existing = wrongQuestionDao.getWrongQuestionById(question.id)
            val wrongCount = (existing?.wrongCount ?: 0) + 1
            val entity = WrongQuestionEntity(
                questionId = question.id,
                gradeLevel = question.grade.level,
                subjectName = question.subject.label,
                title = question.title,
                optionsJson = question.options.joinToString("|||"),
                correctIndex = question.correctIndex,
                userSelectedIndex = selectedIndex,
                explanation = question.explanation,
                sampleEssence = question.sampleEssence,
                wrongCount = wrongCount,
                lastAttemptTimestamp = System.currentTimeMillis(),
                isMastered = false
            )
            wrongQuestionDao.insertOrUpdateWrongQuestion(entity)
        }
    }

    suspend fun markWrongQuestionMastered(questionId: String) {
        wrongQuestionDao.markAsMastered(questionId)
    }

    suspend fun removeWrongQuestion(questionId: String) {
        wrongQuestionDao.deleteWrongQuestion(questionId)
    }

    suspend fun saveUnlockSession(
        total: Int,
        correct: Int,
        gradeLevel: Int,
        isSuccess: Boolean
    ) {
        unlockRecordDao.insertRecord(
            UnlockRecordEntity(
                totalQuestions = total,
                correctQuestions = correct,
                gradeLevel = gradeLevel,
                isSuccess = isSuccess
            )
        )
    }

    fun getDiagnosticStream(grade: Grade): Flow<DiagnosticResult> {
        return combine(
            learningPathDao.getLogsByGrade(grade.level),
            learningPathDao.getSubjectStatsByGrade(grade.level),
            wrongQuestionDao.getAllWrongQuestions()
        ) { logs, stats, wrongs ->
            val total = logs.size
            val correctCount = logs.count { it.isCorrect }
            val overallAcc = if (total > 0) correctCount.toFloat() / total.toFloat() else 0f

            val subjectAccMap = mutableMapOf<Subject, Float>()
            val subjectAnsweredMap = mutableMapOf<Subject, Int>()

            for (subj in Subject.values()) {
                val subjStat = stats.find { it.subjectName == subj.label }
                if (subjStat != null && subjStat.totalCount > 0) {
                    subjectAccMap[subj] = subjStat.correctCount.toFloat() / subjStat.totalCount.toFloat()
                    subjectAnsweredMap[subj] = subjStat.totalCount
                } else {
                    subjectAccMap[subj] = 0f
                    subjectAnsweredMap[subj] = 0
                }
            }

            var weakSubj: Subject? = null
            var strongSubj: Subject? = null

            val testedSubjects = subjectAnsweredMap.filter { it.value > 0 }
            if (testedSubjects.isNotEmpty()) {
                weakSubj = testedSubjects.minByOrNull { subjectAccMap[it.key] ?: 0f }?.key
                strongSubj = testedSubjects.maxByOrNull { subjectAccMap[it.key] ?: 0f }?.key
            }

            val levelName = when {
                total < 3 -> "初学自测期"
                overallAcc >= 0.85f -> "拔尖冲刺 (学霸档)"
                overallAcc >= 0.65f -> "进阶强化 (良好档)"
                else -> "基础夯实 (巩固档)"
            }

            val advice = when {
                total < 3 -> "当前答题样本较少，建议多完成几轮解锁通关答题，以激活精准的学科能力画像！"
                weakSubj == Subject.CHINESE_ESSAY -> "检测到作文与语文表现相对薄弱，建议重点强化拟人修辞、开头特写技巧以及记叙文细节刻画！"
                weakSubj == Subject.MATH -> "检测到数学与思维运算相对薄弱，建议加强简便运算拆分、几何图形内角和及分数/比例性质的变式练习！"
                weakSubj == Subject.ENGLISH -> "检测到小学英语核心语法相对薄弱，建议夯实时态标志词识别、形容词比较级变形与时间介词搭配！"
                overallAcc >= 0.85f -> "三科均衡拔尖！可进行综合思维拓展，挑战高年级交叉大题与深度阅读！"
                else -> "整体水平稳步提升，针对薄弱专项定制每日计划，查漏补缺更高效！"
            }

            val recommendedTopics = mutableListOf<String>()
            when (weakSubj) {
                Subject.CHINESE_ESSAY -> {
                    recommendedTopics.add("记叙文生动拟人手法训练")
                    recommendedTopics.add("考场作文抓人眼球的开头秘籍")
                    recommendedTopics.add("以小见大哲理文言文精读")
                }
                Subject.MATH -> {
                    recommendedTopics.add("乘法结合律速算对 (125×8与25×4)")
                    recommendedTopics.add("几何内角和与等腰直角图形分析")
                    recommendedTopics.add("比例尺与立体几何体积核心模型")
                }
                Subject.ENGLISH -> {
                    recommendedTopics.add("现在进行时与一般过去时考点专攻")
                    recommendedTopics.add("高频时间介词(at/on/in)口诀运用")
                    recommendedTopics.add("形容词比较级规则与不规则变形速记")
                }
                null -> {
                    recommendedTopics.add("语文作文佳句与修辞手把手")
                    recommendedTopics.add("数学核心速算思维闯关")
                    recommendedTopics.add("小学核心句型语感训练")
                }
            }

            DiagnosticResult(
                grade = grade,
                totalAnswered = total,
                overallAccuracy = overallAcc,
                subjectAccuracyMap = subjectAccMap,
                subjectAnsweredMap = subjectAnsweredMap,
                weakSubject = weakSubj,
                strongSubject = strongSubj,
                masteryLevel = levelName,
                advice = advice,
                recommendedTopics = recommendedTopics
            )
        }
    }

    suspend fun generateOrUpdatePlan(diagnostic: DiagnosticResult) {
        val plan = LearningPlanEntity(
            gradeLevel = diagnostic.grade.level,
            currentLevelName = diagnostic.masteryLevel,
            weakSubject = diagnostic.weakSubject?.label ?: "全科均衡",
            strongSubject = diagnostic.strongSubject?.label ?: "待测评",
            dailyTargetQuestions = if (diagnostic.overallAccuracy < 0.7f) 6 else 4,
            recommendedTopic = diagnostic.recommendedTopics.firstOrNull() ?: "基础巩固综合包",
            studyAdvice = diagnostic.advice,
            updatedAt = System.currentTimeMillis()
        )
        learningPathDao.saveLearningPlan(plan)
    }

    suspend fun getAdaptiveRecommendations(diagnostic: DiagnosticResult): List<RecommendedPractice> {
        val gradeQuestions = getQuestionsForGrade(diagnostic.grade)
        val result = mutableListOf<RecommendedPractice>()

        // 1. Weak Subject Targeted Practice
        val targetSubject = diagnostic.weakSubject ?: Subject.CHINESE_ESSAY
        val weakQuestions = gradeQuestions.filter { it.subject == targetSubject }
        if (weakQuestions.isNotEmpty()) {
            result.add(
                RecommendedPractice(
                    title = "🎯 专项补弱：${targetSubject.label}定向攻坚",
                    subject = targetSubject,
                    difficulty = if (diagnostic.overallAccuracy < 0.6f) "基础巩固" else "强化提高",
                    reason = "根据学习分析，您在${targetSubject.label}的错题较集中，推荐本练习快速查漏补缺",
                    questions = weakQuestions
                )
            )
        }

        // 2. High Frequency Exam Mastery
        val otherSubject = if (targetSubject == Subject.MATH) Subject.ENGLISH else Subject.MATH
        val highFreqQuestions = gradeQuestions.filter { it.subject == otherSubject }
        if (highFreqQuestions.isNotEmpty()) {
            result.add(
                RecommendedPractice(
                    title = "⚡ 思维拓展：${otherSubject.label}高频考点演练",
                    subject = otherSubject,
                    difficulty = "能力进阶",
                    reason = "紧扣${diagnostic.grade.label}重点题型，帮助孩子拓宽学科思维",
                    questions = highFreqQuestions
                )
            )
        }

        // 3. Composition & Expression Elite Set
        val essayQuestions = gradeQuestions.filter { it.subject == Subject.CHINESE_ESSAY }
        if (essayQuestions.isNotEmpty() && targetSubject != Subject.CHINESE_ESSAY) {
            result.add(
                RecommendedPractice(
                    title = "✍️ 作文锦囊：语言表达与构思特训",
                    subject = Subject.CHINESE_ESSAY,
                    difficulty = "素养提升",
                    reason = "积累满分作文好句、修辞手法与立意技法，全面提高写作兴趣",
                    questions = essayQuestions
                )
            )
        }

        return result
    }
}
