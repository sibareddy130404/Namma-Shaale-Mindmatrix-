package com.nammashale.assetmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammashale.assetmanager.ui.theme.ConditionBroken
import com.nammashale.assetmanager.ui.theme.ConditionNeedsRepair
import com.nammashale.assetmanager.ui.theme.ConditionWorking

/**
 * Color-coded chip showing asset condition.
 * 🟢 Working | 🟡 Needs Repair | 🔴 Broken
 */
@Composable
fun ConditionChip(condition: String, modifier: Modifier = Modifier) {
    val color = when (condition) {
        "Working" -> ConditionWorking
        "Needs Repair" -> ConditionNeedsRepair
        "Broken" -> ConditionBroken
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = condition,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}
