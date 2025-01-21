package ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitcompose.modifier.borderBottom
import ui.app.theme.AppTheme

@Composable
fun Tab(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .then(if (isActive) Modifier.borderBottom(AppTheme.colors.primary, 2.dp) else Modifier)
            .height(40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}