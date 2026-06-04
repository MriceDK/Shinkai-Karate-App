package be.mauricedeke.shinkai.ui.profiel.strength

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.displayName
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.toColor

@Composable
fun StrengthTestScreen(
    uiState: StrengthTestUiState,
    onStartTest: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (uiState.isError) {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                Text(
                    "Kon resultaten niet laden. Probeer opnieuw.",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }
        } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            uiState.results.forEachIndexed { i, result ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp).align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            result.type,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textDecoration = TextDecoration.Underline
                        )
                        Spacer(Modifier.size(16.dp))
                        val beltColor =
                            result.beltColor?.toColor(isDark) ?: MaterialTheme.colorScheme.primary
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 56.dp, height = 18.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(beltColor)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(beltColor.copy(alpha = 0.7f))
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "${result.score} ${result.unit}",
                                color = beltColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 48.sp
                            )
                        }
                        if (result.beltColor != null) {
                            Spacer(Modifier.size(4.dp))
                            Text(
                                result.beltColor.displayName(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = beltColor
                            )
                        }
                        Spacer(Modifier.size(16.dp))
                        Button(
                            onClick = { onStartTest(result.type) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text("Start", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
        } // end else
        RoundBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 8.dp)
        )
    }
}

@Preview(name = "No results yet", showBackground = true, showSystemUi = true)
@Composable
fun StrengthTestScreenEmptyPreview() {
    ShinkaikarateappTheme { StrengthTestScreen(uiState = StrengthTestUiState()) }
}

@Preview(name = "With results", showBackground = true, showSystemUi = true)
@Composable
fun StrengthTestScreenWithResultsPreview() {
    ShinkaikarateappTheme {
        StrengthTestScreen(
            uiState = StrengthTestUiState(
                results = listOf(
                    be.mauricedeke.shinkai.domain.model.StrengthResult(
                        type = "Kiai",
                        score = 78,
                        beltColor = be.mauricedeke.shinkai.domain.model.BeltColor.BLUE,
                        unit = "dB"
                    ),
                    be.mauricedeke.shinkai.domain.model.StrengthResult(
                        type = "Stoot",
                        score = 642,
                        beltColor = be.mauricedeke.shinkai.domain.model.BeltColor.PURPLE,
                        unit = "N"
                    )
                )
            )
        )
    }
}

@Preview(name = "Error", showBackground = true, showSystemUi = true)
@Composable
fun StrengthTestScreenErrorPreview() {
    ShinkaikarateappTheme { StrengthTestScreen(uiState = StrengthTestUiState(isError = true)) }
}
