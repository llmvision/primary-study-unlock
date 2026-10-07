package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Grade
import com.example.data.model.Question
import com.example.data.repository.QuestionBank
import com.example.data.repository.StudyRepository
import com.example.ui.components.SubjectBadge
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import kotlinx.coroutines.launch

@Composable
fun UnlockQuizScreen(
    grade: Grade,
    repository: StudyRepository,
    customQuestions: List<Question>? = null,
    onUnlockFinished: (Boolean, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    // Quiz session questions
    val quizQuestions = remember(grade, customQuestions) {
        customQuestions?.ifEmpty { null } ?: QuestionBank.getRandomQuiz(grade, count = 3)
    }

    var currentIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var hasSubmittedAnswer by remember { mutableStateOf(false) }
    var correctCount by remember { mutableStateOf(0) }

    val currentQuestion = quizQuestions.getOrNull(currentIndex)

    if (currentQuestion == null) {
        // Fallback or finish
        return
    }

    val progress = (currentIndex + 1).toFloat() / quizQuestions.size.toFloat()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LockOpen,
                    contentDescription = null,
                    tint = IndigoPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${grade.label} · 通关答题解锁",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IndigoPrimary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${currentIndex + 1} / ${quizQuestions.size}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = IndigoPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Indicator
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = IndigoPrimary,
            trackColor = IndigoPrimary.copy(alpha = 0.15f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("question_card"),
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
                    SubjectBadge(subject = currentQuestion.subject)
                    Text(
                        text = "需回答3题方可解锁",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentQuestion.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options List
        val optionLabels = listOf("A", "B", "C", "D")
        currentQuestion.options.forEachIndexed { index, optionText ->
            val isSelected = selectedOptionIndex == index
            val isCorrect = index == currentQuestion.correctIndex

            val borderAndBgColor = when {
                !hasSubmittedAnswer && isSelected -> Pair(IndigoPrimary, IndigoPrimary.copy(alpha = 0.08f))
                hasSubmittedAnswer && isCorrect -> Pair(SuccessGreen, SuccessGreenLight)
                hasSubmittedAnswer && isSelected && !isCorrect -> Pair(ErrorRed, ErrorRedLight)
                else -> Pair(Color(0xFFE2E8F0), MaterialTheme.colorScheme.surface)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(enabled = !hasSubmittedAnswer) {
                        selectedOptionIndex = index
                    }
                    .testTag("option_item_$index"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, borderAndBgColor.first),
                colors = CardDefaults.cardColors(containerColor = borderAndBgColor.second)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    hasSubmittedAnswer && isCorrect -> SuccessGreen
                                    hasSubmittedAnswer && isSelected && !isCorrect -> ErrorRed
                                    isSelected -> IndigoPrimary
                                    else -> Color(0xFFF1F5F9)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (hasSubmittedAnswer && isCorrect) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        } else if (hasSubmittedAnswer && isSelected && !isCorrect) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        } else {
                            Text(
                                text = optionLabels.getOrElse(index) { "?" },
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF475569)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Explanation & Keypoints Section (Visible immediately after user submits answer)
        AnimatedVisibility(visible = hasSubmittedAnswer) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("explanation_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
                border = BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = IndigoPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "名师深度解析",
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentQuestion.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1E293B),
                        lineHeight = 22.sp
                    )

                    if (currentQuestion.sampleEssence.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberAccent.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 ${currentQuestion.sampleEssence}",
                                modifier = Modifier.padding(10.dp),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFB45309)
                            )
                        }
                    }

                    if (currentQuestion.keyPoints.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "重点考点提炼：",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF334155)
                        )
                        currentQuestion.keyPoints.forEach { pt ->
                            Text(
                                text = "• $pt",
                                fontSize = 13.sp,
                                color = Color(0xFF475569),
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        if (!hasSubmittedAnswer) {
            Button(
                onClick = {
                    val sel = selectedOptionIndex ?: return@Button
                    hasSubmittedAnswer = true
                    val isCorrect = (sel == currentQuestion.correctIndex)
                    if (isCorrect) {
                        correctCount++
                    }
                    coroutineScope.launch {
                        repository.recordAnswerResult(currentQuestion, sel, isCorrect)
                    }
                },
                enabled = selectedOptionIndex != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_answer_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text("提交答案，查看详细解析", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            val isLast = currentIndex >= quizQuestions.size - 1
            Button(
                onClick = {
                    if (isLast) {
                        val isSuccess = correctCount >= 2 // At least 2 correct out of 3 to unlock
                        coroutineScope.launch {
                            repository.saveUnlockSession(
                                total = quizQuestions.size,
                                correct = correctCount,
                                gradeLevel = grade.level,
                                isSuccess = isSuccess
                            )
                        }
                        onUnlockFinished(isSuccess, correctCount, quizQuestions.size)
                    } else {
                        currentIndex++
                        selectedOptionIndex = null
                        hasSubmittedAnswer = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("next_question_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLast) SuccessGreen else IndigoPrimary
                )
            ) {
                Text(
                    text = if (isLast) "完成通关挑战" else "下一题",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
