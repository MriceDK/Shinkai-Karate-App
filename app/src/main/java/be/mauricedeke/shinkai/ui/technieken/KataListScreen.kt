package be.mauricedeke.shinkai.ui.technieken

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.R
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.Kata
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.toColor

@Composable
fun KataListScreen(
    uiState: TechniekUiState,
    modifier: Modifier = Modifier
) {
    if (uiState.isKataError) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Kon kata's niet laden. Probeer opnieuw.",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier.padding(32.dp)
            )
        }
        return
    }

    if (uiState.katas.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Geen kata's gevonden.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.katas, key = { it.id }) { kata ->
            KataCard(kata = kata)
        }
    }
}

@Composable
private fun KataCard(kata: Kata) {
    val isDark = isSystemInDarkTheme()
    val beltColor = kata.beltColor.toColor(isDark)
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(width = 36.dp, height = 12.dp)
                            .clip(RoundedCornerShape(50))
                            .background(beltColor)
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(beltColor.copy(alpha = 0.7f))
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        kata.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        kata.belt,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    painterResource(if (expanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        if (kata.description.isNotBlank()) {
                            Text(
                                kata.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                        if (kata.moves.isNotEmpty()) {
                            Text(
                                "Bewegingen",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(4.dp))
                            kata.moves.forEachIndexed { index, move ->
                                Text(
                                    "${index + 1}. $move",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val previewKatas = listOf(
    Kata(
        "1",
        "Pinan Shodan",
        "Geel",
        BeltColor.YELLOW,
        "Eerste kata voor beginners.",
        listOf("Yoi", "Hidari gedan barai", "Migi oi-zuki", "Migi gedan barai")
    ),
    Kata(
        "2",
        "Pinan Nidan",
        "Oranje",
        BeltColor.ORANGE,
        "Tweede kata in de Pinan serie.",
        listOf("Yoi", "Migi gedan barai", "Hidari oi-zuki", "Migi uraken")
    ),
    Kata(
        "3",
        "Pinan Sandan",
        "Rood",
        BeltColor.RED,
        "Derde kata met meer complexe combinaties.",
        listOf("Yoi", "Hidari gedan barai", "Migi oi-zuki")
    ),
)

@Preview(name = "Kata List", showBackground = true, showSystemUi = true)
@Composable
fun KataListScreenPreview() {
    ShinkaikarateappTheme { KataListScreen(uiState = TechniekUiState(katas = previewKatas)) }
}

@Preview(
    name = "Kata List Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun KataListScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) { KataListScreen(uiState = TechniekUiState(katas = previewKatas)) }
}
