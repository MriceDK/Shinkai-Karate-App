package be.mauricedeke.shinkai.ui.profiel.strength.punch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.displayName
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.toColor

@Composable
fun PunchTestScreen(
    uiState: PunchTestUiState,
    onStart: () -> Unit = {},
    onReset: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progressAnim by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(50),
        label = "progress"
    )

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Punch Force Test",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(8.dp))

            Text(
                when (uiState.phase) {
                    MeasurementPhase.IDLE -> "Hold the phone firmly and punch as hard as you can."
                    MeasurementPhase.MEASURING -> "PUNCH NOW!"
                    MeasurementPhase.DONE -> "Your result"
                },
                fontSize = 14.sp,
                color = if (uiState.phase == MeasurementPhase.MEASURING) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (uiState.phase == MeasurementPhase.MEASURING) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
                CircularProgressIndicator(
                    progress = { progressAnim },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 10.dp,
                    color = uiState.resultBelt?.toColor() ?: MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        uiState.score.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 64.sp,
                        color = uiState.resultBelt?.toColor() ?: MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "N",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Belt result shown after measurement
            if (uiState.resultBelt != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 14.dp)
                            .background(uiState.resultBelt.toColor(), RoundedCornerShape(50))
                    )
                    Text(
                        uiState.resultBelt.displayName(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = uiState.resultBelt.toColor()
                    )
                }
            } else {
                Spacer(Modifier.height(22.dp))
            }

            Spacer(Modifier.height(8.dp))

            // Best score
            if (uiState.bestBelt != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Best:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Box(
                        modifier = Modifier
                            .size(width = 18.dp, height = 8.dp)
                            .background(uiState.bestBelt.toColor(), RoundedCornerShape(50))
                    )
                    Text(
                        "${uiState.bestScore} N",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(Modifier.height(20.dp))
            }

            Spacer(Modifier.height(40.dp))

            when (uiState.phase) {
                MeasurementPhase.IDLE -> Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(160.dp)
                ) { Text("Start", fontSize = 16.sp) }

                MeasurementPhase.MEASURING -> OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(160.dp)
                ) { Text("Cancel", fontSize = 16.sp) }

                MeasurementPhase.DONE -> Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(160.dp)
                ) { Text("Try Again", fontSize = 16.sp) }
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
fun PunchTestScreenPreview() {
    ShinkaikarateappTheme {
        PunchTestScreen(
            uiState = PunchTestUiState(
                phase = MeasurementPhase.DONE,
                score = 659,
                bestScore = 659,
                resultBelt = BeltColor.PURPLE,
                bestBelt = BeltColor.PURPLE,
                progress = 1f
            )
        )
    }
}
