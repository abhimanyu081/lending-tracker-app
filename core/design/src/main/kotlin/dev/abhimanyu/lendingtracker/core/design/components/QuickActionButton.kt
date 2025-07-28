package dev.abhimanyu.lendingtracker.core.design.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.abhimanyu.lendingtracker.core.design.theme.LendingTrackerTheme

@Composable
fun QuickActionButton(
    text: String,
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true
) {
    val colors = if (isPrimary) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary
        )
    }
    
    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = colors,
            shape = MaterialTheme.shapes.medium
        ) {
            QuickActionContent(emoji = emoji, text = text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = colors,
            shape = MaterialTheme.shapes.medium
        ) {
            QuickActionContent(emoji = emoji, text = text)
        }
    }
}

@Composable
private fun QuickActionContent(
    emoji: String,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuickActionButtonPreview() {
    LendingTrackerTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton(
                text = "LEND\nMONEY",
                emoji = "💸",
                onClick = {},
                modifier = Modifier.weight(1f),
                isPrimary = true
            )
            QuickActionButton(
                text = "BORROW\nMONEY",
                emoji = "💰",
                onClick = {},
                modifier = Modifier.weight(1f),
                isPrimary = false
            )
        }
    }
}