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
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WrongQuestionEntity
import com.example.data.repository.StudyRepository
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.ChineseOrange
import com.example.ui.theme.EnglishPurple
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MathBlue
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WrongBookScreen(
    repository: StudyRepository,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val wrongQuestions by repository.allWrongQuestions.collectAsState(initial = emptyList())

    var selectedGradeFilter by remember { mutableStateOf<Int?>(null) } // null = all
    var selectedSubjectFilter by remember { mutableStateOf<String?>(null) }

    val filteredList = wrongQuestions.filter { item ->
        (selectedGradeFilter == null || item.gradeLevel == selectedGradeFilter) &&
                (selectedSubjectFilter == null || item.subjectName == selectedSubjectFilter)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Title Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "错题回顾与精析",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "温故而知新，掌握每个疑难考点",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorRed.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "共收录 ${wrongQuestions.size} 题",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grade Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedGradeFilter == null,
                onClick = { selectedGradeFilter = null },
                label = { Text("全部年级") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IndigoPrimary.copy(alpha = 0.15f),
                    selectedLabelColor = IndigoPrimary
                )
            )
            listOf(4 to "四年级", 5 to "五年级", 6 to "六年级").forEach { (lvl, title) ->
                FilterChip(
                    selected = selectedGradeFilter == lvl,
                    onClick = { selectedGradeFilter = if (selectedGradeFilter == lvl) null else lvl },
                    label = { Text(title) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IndigoPrimary.copy(alpha = 0.15f),
                        selectedLabelColor = IndigoPrimary
                    )
                )
            }
        }

        // Subject Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedSubjectFilter == null,
                onClick = { selectedSubjectFilter = null },
                label = { Text("全部学科") }
            )
            listOf("语文与作文", "数学与逻辑", "小学核心英语").forEach { subj ->
                FilterChip(
                    selected = selectedSubjectFilter == subj,
                    onClick = { selectedSubjectFilter = if (selectedSubjectFilter == subj) null else subj },
                    label = { Text(subj) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (wrongQuestions.isEmpty()) "太棒啦！暂无错题记录" else "当前筛选条件下无错题",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "继续在通关答题中挑战，巩固4-6年级知识！",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList, key = { it.questionId }) { item ->
                    WrongQuestionCard(
                        item = item,
                        onMarkMastered = {
                            coroutineScope.launch {
                                repository.markWrongQuestionMastered(item.questionId)
                            }
                        },
                        onDelete = {
                            coroutineScope.launch {
                                repository.removeWrongQuestion(item.questionId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WrongQuestionCard(
    item: WrongQuestionEntity,
    onMarkMastered: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val options = remember(item.optionsJson) {
        item.optionsJson.split("|||")
    }

    val (badgeBg, badgeText) = when (item.subjectName) {
        "语文与作文" -> Pair(ChineseOrange.copy(alpha = 0.12f), ChineseOrange)
        "数学与逻辑" -> Pair(MathBlue.copy(alpha = 0.12f), MathBlue)
        else -> Pair(EnglishPurple.copy(alpha = 0.12f), EnglishPurple)
    }

    val dateFormatter = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("wrong_card_${item.questionId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isMastered) Color(0xFFF8FAFC) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (item.isMastered) SuccessGreen.copy(alpha = 0.3f) else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = badgeBg
                    ) {
                        Text(
                            text = "${item.gradeLevel}年级 · ${item.subjectName}",
                            color = badgeText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    if (item.isMastered) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SuccessGreen.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "已掌握",
                                color = SuccessGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "错题次数: ${item.wrongCount}次",
                    fontSize = 11.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Options summary
            val optionLabels = listOf("A", "B", "C", "D")
            options.forEachIndexed { idx, opt ->
                val isCorrect = idx == item.correctIndex
                val wasSelected = idx == item.userSelectedIndex

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                isCorrect -> SuccessGreen.copy(alpha = 0.1f)
                                wasSelected -> ErrorRed.copy(alpha = 0.08f)
                                else -> Color.Transparent
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${optionLabels.getOrElse(idx) { "?" }}. $opt",
                        fontSize = 13.sp,
                        color = when {
                            isCorrect -> SuccessGreen
                            wasSelected -> ErrorRed
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        },
                        fontWeight = if (isCorrect || wasSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )

                    if (isCorrect) {
                        Text(" (正确答案)", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                    } else if (wasSelected) {
                        Text(" (你的选择)", fontSize = 11.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Accordion Button for Deep Explanation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = IndigoPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isExpanded) "收起详细解析" else "查看详细解析与名师要点",
                        color = IndigoPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = IndigoPrimary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "【解析精讲】",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = item.explanation,
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    if (item.sampleEssence.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "【名师干货 / 佳句技巧】",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberAccent
                        )
                        Text(
                            text = item.sampleEssence,
                            fontSize = 12.sp,
                            color = Color(0xFFB45309),
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            // Actions footer
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!item.isMastered) {
                    OutlinedButton(
                        onClick = onMarkMastered,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp), tint = SuccessGreen)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("标记已攻克", fontSize = 12.sp, color = SuccessGreen)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkRemove,
                        contentDescription = "删除错题",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
