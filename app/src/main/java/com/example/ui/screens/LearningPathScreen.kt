package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiagnosticResult
import com.example.data.model.Grade
import com.example.data.model.Question
import com.example.data.model.RecommendedPractice
import com.example.data.model.Subject
import com.example.data.repository.StudyRepository
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.ChineseOrange
import com.example.ui.theme.EnglishPurple
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MathBlue
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.launch

@Composable
fun LearningPathScreen(
    currentGrade: Grade,
    repository: StudyRepository,
    onStartPractice: (List<Question>) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val diagnosticResult by repository.getDiagnosticStream(currentGrade).collectAsState(
        initial = DiagnosticResult(
            grade = currentGrade,
            totalAnswered = 0,
            overallAccuracy = 0f,
            subjectAccuracyMap = emptyMap(),
            subjectAnsweredMap = emptyMap(),
            weakSubject = null,
            strongSubject = null,
            masteryLevel = "自测诊断中",
            advice = "正在生成针对各学科的学习建议...",
            recommendedTopics = emptyList()
        )
    )

    val currentPlan by repository.currentLearningPlan.collectAsState(initial = null)
    var adaptivePractices by remember { mutableStateOf<List<RecommendedPractice>>(emptyList()) }

    androidx.compose.runtime.LaunchedEffect(diagnosticResult) {
        adaptivePractices = repository.getAdaptiveRecommendations(diagnosticResult)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = null,
                            tint = IndigoPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "个性化学习路径",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "基于答题正确率与错题分析，动态规划学科进阶",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IndigoPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "${currentGrade.label}画像",
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Diagnostic Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("diagnostic_overview_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "当前学情诊断",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                diagnosticResult.overallAccuracy >= 0.8f -> SuccessGreen.copy(alpha = 0.12f)
                                diagnosticResult.overallAccuracy >= 0.6f -> AmberAccent.copy(alpha = 0.15f)
                                else -> ErrorRed.copy(alpha = 0.1f)
                            }
                        ) {
                            Text(
                                text = diagnosticResult.masteryLevel,
                                color = when {
                                    diagnosticResult.overallAccuracy >= 0.8f -> SuccessGreen
                                    diagnosticResult.overallAccuracy >= 0.6f -> Color(0xFFB45309)
                                    else -> ErrorRed
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Accuracy Bars for 3 Subjects
                    SubjectAccuracyRow(
                        subject = Subject.CHINESE_ESSAY,
                        answeredCount = diagnosticResult.subjectAnsweredMap[Subject.CHINESE_ESSAY] ?: 0,
                        accuracy = diagnosticResult.subjectAccuracyMap[Subject.CHINESE_ESSAY] ?: 0f,
                        color = ChineseOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SubjectAccuracyRow(
                        subject = Subject.MATH,
                        answeredCount = diagnosticResult.subjectAnsweredMap[Subject.MATH] ?: 0,
                        accuracy = diagnosticResult.subjectAccuracyMap[Subject.MATH] ?: 0f,
                        color = MathBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SubjectAccuracyRow(
                        subject = Subject.ENGLISH,
                        answeredCount = diagnosticResult.subjectAnsweredMap[Subject.ENGLISH] ?: 0,
                        accuracy = diagnosticResult.subjectAccuracyMap[Subject.ENGLISH] ?: 0f,
                        color = EnglishPurple
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // AI Diagnostic Advice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = AmberAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "学情分析与调整策略",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = diagnosticResult.advice,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = Color(0xFF475569),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                repository.generateOrUpdatePlan(diagnosticResult)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("update_plan_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("根据最新学情更新个性化学习计划", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active Adaptive Learning Plan Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_plan_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoPrimary.copy(alpha = 0.05f)),
                border = BorderStroke(1.2.dp, IndigoPrimary.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "今日量身定制规划",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = IndigoPrimary
                            )
                        }

                        Text(
                            text = "每日目标: ${currentPlan?.dailyTargetQuestions ?: 5} 题",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IndigoPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "本阶段攻坚专题：${currentPlan?.recommendedTopic ?: (diagnosticResult.recommendedTopics.firstOrNull() ?: "基础语文与数学重点综合")}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "优势科目：${currentPlan?.strongSubject ?: (diagnosticResult.strongSubject?.label ?: "测评中")}   |   重点突破：${currentPlan?.weakSubject ?: (diagnosticResult.weakSubject?.label ?: "待诊断")}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Adaptive Practice Recommendations Header
        item {
            Text(
                text = "系统智能推荐强化习题",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // List of Adaptive Practice Sets
        items(adaptivePractices) { practice ->
            AdaptivePracticeCard(
                practice = practice,
                onStart = {
                    onStartPractice(practice.questions)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SubjectAccuracyRow(
    subject: Subject,
    answeredCount: Int,
    accuracy: Float,
    color: Color
) {
    val percentInt = (accuracy * 100).toInt()
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SubjectBadge(subject = subject)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "已答 $answeredCount 题",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "正确率 $percentInt%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { accuracy },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}

@Composable
fun AdaptivePracticeCard(
    practice: RecommendedPractice,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("practice_card_${practice.subject.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubjectBadge(subject = practice.subject)

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AmberAccent.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = practice.difficulty,
                        color = Color(0xFFB45309),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = practice.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = practice.reason,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "共精选 ${practice.questions.size} 道针对性习题",
                    fontSize = 12.sp,
                    color = IndigoPrimary,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("开始强化练习", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
