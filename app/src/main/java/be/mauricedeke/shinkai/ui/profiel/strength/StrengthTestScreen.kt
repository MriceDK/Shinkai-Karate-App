package be.mauricedeke.shinkai.ui.profiel.strength

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.StrengthResult
import be.mauricedeke.shinkai.ui.theme.BeltGreen
import be.mauricedeke.shinkai.ui.theme.BeltOrange
import be.mauricedeke.shinkai.ui.theme.ShinkaiGray
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun StrengthTestScreen(
    uiState: StrengthTestUiState,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val results = uiState.results.ifEmpty {
        listOf(
            StrengthResult("Punching Strength", 659, "#1B5E20"),
            StrengthResult("Kiai Strength", 250, "#FF8C00")
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ShinkaiGray),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            results.forEachIndexed { i, result ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        result.type,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textDecoration = TextDecoration.Underline
                    )
                    Spacer(Modifier.size(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(width = 56.dp, height = 18.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (i == 0) BeltGreen else BeltOrange)
                            )
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        if (i == 0) BeltGreen.copy(alpha = 0.7f) else BeltOrange.copy(
                                            alpha = 0.7f
                                        )
                                    )
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            result.score.toString(),
                            color = ShinkaiRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 56.sp
                        )
                    }
                    Spacer(Modifier.size(16.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Start", fontSize = 16.sp)
                    }
                }
            }
        }
        RoundBackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StrengthTestScreenPreview() {
    ShinkaikarateappTheme { StrengthTestScreen(uiState = StrengthTestUiState()) }
}
