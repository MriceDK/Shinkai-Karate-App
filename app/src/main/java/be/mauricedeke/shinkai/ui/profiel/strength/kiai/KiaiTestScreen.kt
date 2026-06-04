package be.mauricedeke.shinkai.ui.profiel.strength.kiai

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.domain.model.displayName
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.toColor

@Composable
fun KiaiTestScreen(
    uiState: KiaiTestUiState,
    onStart: () -> Unit = {},
    onReset: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val progressAnim by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(50),
        label = "progress"
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) onStart() }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Kiai Strength Test",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    when (uiState.phase) {
                        MeasurementPhase.IDLE -> "Give your loudest kiai for 3 seconds."
                        MeasurementPhase.MEASURING -> "KIAI!"
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
                        color = uiState.resultBelt?.toColor(isDark) ?: MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                    Box(
                        modifier = Modifier
                            .size(170.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (uiState.phase == MeasurementPhase.MEASURING) uiState.currentDb.toString()
                                else uiState.peakDb.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 64.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                "dB",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (uiState.resultBelt != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 40.dp, height = 14.dp)
                                .background(uiState.resultBelt.toColor(isDark), RoundedCornerShape(50))
                        )
                        Text(
                            uiState.resultBelt.displayName(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = uiState.resultBelt.toColor(isDark)
                        )
                    }
                } else {
                    Spacer(Modifier.height(22.dp))
                }

                Spacer(Modifier.height(8.dp))

                if (uiState.bestBelt != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Best:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Box(
                            modifier = Modifier
                                .size(width = 18.dp, height = 8.dp)
                                .background(uiState.bestBelt.toColor(isDark), RoundedCornerShape(50))
                        )
                        Text(
                            "${uiState.bestDb} dB",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Spacer(Modifier.height(20.dp))
                }

                Spacer(Modifier.height(32.dp))

                when (uiState.phase) {
                    MeasurementPhase.IDLE -> Button(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                onStart()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
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
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                onStart()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(160.dp)
                    ) { Text("Try Again", fontSize = 16.sp) }
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

@Preview(name = "Idle — ready to start", showBackground = true, showSystemUi = true)
@Composable
fun KiaiTestScreenIdlePreview() {
    ShinkaikarateappTheme {
        KiaiTestScreen(uiState = KiaiTestUiState(phase = MeasurementPhase.IDLE))
    }
}

@Preview(name = "Measuring — in progress", showBackground = true, showSystemUi = true)
@Composable
fun KiaiTestScreenMeasuringPreview() {
    ShinkaikarateappTheme {
        KiaiTestScreen(
            uiState = KiaiTestUiState(
                phase = MeasurementPhase.MEASURING,
                currentDb = 82,
                peakDb = 88,
                progress = 0.6f
            )
        )
    }
}

@Preview(name = "Done — with result", showBackground = true, showSystemUi = true)
@Composable
fun KiaiTestScreenDonePreview() {
    ShinkaikarateappTheme {
        KiaiTestScreen(
            uiState = KiaiTestUiState(
                phase = MeasurementPhase.DONE,
                peakDb = 75,
                bestDb = 75,
                resultBelt = BeltColor.BLUE,
                bestBelt = BeltColor.BLUE,
                progress = 1f
            )
        )
    }
}

@Preview(name = "Dark — done", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun KiaiTestScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) {
        KiaiTestScreen(
            uiState = KiaiTestUiState(
                phase = MeasurementPhase.DONE,
                peakDb = 68,
                bestDb = 75,
                resultBelt = BeltColor.GREEN,
                bestBelt = BeltColor.BLUE,
                progress = 0.8f
            )
        )
    }
}
