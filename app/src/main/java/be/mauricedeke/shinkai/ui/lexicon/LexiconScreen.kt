package be.mauricedeke.shinkai.ui.lexicon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.LexiconEntry
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun LexiconScreen(
    uiState: LexiconUiState,
    onSearchQueryChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("search", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            trailingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ShinkaiRed,
                unfocusedBorderColor = ShinkaiRed
            ),
            singleLine = true
        )
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)) {
            items(uiState.filteredEntries) { entry -> LexiconRow(entry) }
        }
    }
}

@Composable
private fun LexiconRow(entry: LexiconEntry) {
    var expanded by remember { mutableStateOf(false) }
    val expandedText = entry.description.ifEmpty { entry.translation }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, ShinkaiRed, RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                entry.japaneseWord,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text("|", color = ShinkaiRed, fontSize = 18.sp)
            Text(
                entry.translation,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = ShinkaiRed,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.size(8.dp))
        }
        AnimatedVisibility(visible = expanded) {
            Column {
                HorizontalDivider(color = ShinkaiRed.copy(alpha = 0.3f))
                Text(
                    expandedText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LexiconScreenPreview() {
    ShinkaikarateappTheme {
        LexiconScreen(
            uiState = LexiconUiState(
                entries = listOf(
                    LexiconEntry("Rei", "Buiging"),
                    LexiconEntry("Dojo", "Trainingsplaats")
                )
            )
        )
    }
}
