package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Subject
import com.example.ui.theme.ChineseOrange
import com.example.ui.theme.EnglishPurple
import com.example.ui.theme.MathBlue

@Composable
fun SubjectBadge(
    subject: Subject,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (subject) {
        Subject.CHINESE_ESSAY -> Pair(ChineseOrange.copy(alpha = 0.12f), ChineseOrange)
        Subject.MATH -> Pair(MathBlue.copy(alpha = 0.12f), MathBlue)
        Subject.ENGLISH -> Pair(EnglishPurple.copy(alpha = 0.12f), EnglishPurple)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = subject.label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
