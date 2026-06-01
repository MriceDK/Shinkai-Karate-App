package be.mauricedeke.shinkai.ui.technieken.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Techniek
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun TechniekDetailScreen(
    uiState: TechniekDetailUiState,
    onNotesChanged: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val belt = uiState.belt.ifBlank { "Geel" }
    val technieken = uiState.technieken.ifEmpty {
        listOf(
            Techniek("Gekruis vastnemen 1 hand", belt),
            Techniek("Parallel vastnemen 1 hand", belt),
            Techniek("Vastnemen 2 handen op 1", belt),
            Techniek(
                "Wurging met 1 hand en hoekstoot", belt,
                "Kin naar beneden doen en met de rechter hand de linkse hoekstoot blokkeren " +
                        "en met de linkerhand controle uitoefenen op de rechtse hand van de aanvaller."
            ),
        )
    }
    val expandedStates =
        remember(technieken) { technieken.map { mutableStateOf(it == technieken.last()) } }
    var programmaExpanded by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 58.dp, horizontal = 16.dp)
        ) {
            AccordionHeader("Programma", programmaExpanded) {
                programmaExpanded = !programmaExpanded
            }
            if (programmaExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                        )
                        .padding(16.dp)
                ) {
                    ProgrammaContent()
                }
            }
            Spacer(Modifier.height(16.dp))
            technieken.forEachIndexed { i, t ->
                AccordionHeader(t.name, expandedStates[i].value) {
                    expandedStates[i].value = !expandedStates[i].value
                }
                if (expandedStates[i].value && t.description.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            t.description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Alle technieken dienen links en rechts uitgevoerd te worden",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                    .padding(2.dp)
            ) {
                Column {
                    Text(
                        "Notes",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                    )
                    OutlinedTextField(
                        value = uiState.notes,
                        onValueChange = onNotesChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        minLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        RoundBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 8.dp)
        )
    }
}

@Composable
private fun AccordionHeader(title: String, expanded: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                if (expanded) RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp
                ) else RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ProgrammaContent() {
    Text(
        "10 bevrijdingstechnieken :",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    listOf(
        "Tegen gekruist vastnemen hand",
        "Tegen parallel vastnemen hand",
        "Tegen vastnemen aan beide handen",
        "Tegen vastnemen twee handen op één",
        "Tegen wurging met één hand en hoekstoot"
    )
        .forEach {
            Text(
                "- $it",
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    Spacer(Modifier.height(6.dp))
    Text(
        "2 Technieken vrije keuze self defense",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.height(6.dp))
    Text(buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        ) { append("Traptechnieken / geri waza") }
    }, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
    listOf(
        "Mae Geri (voorwaartse trap)",
        "Mawashi Geri (cirkelvormige trap)",
        "Yoko Geri (zijwaartse trap)",
        "Ushiro Geri (achterwaartse trap)"
    )
        .forEach {
            Text(
                "- $it",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp),
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    Spacer(Modifier.height(6.dp))
    Text(
        "Kata",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    listOf("Shodan Kata", "Blokkingset 1").forEach {
        Text(
            "- $it",
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TechniekDetailScreenPreview() {
    ShinkaikarateappTheme {
        TechniekDetailScreen(uiState = TechniekDetailUiState(belt = "Geel"))
    }
}
