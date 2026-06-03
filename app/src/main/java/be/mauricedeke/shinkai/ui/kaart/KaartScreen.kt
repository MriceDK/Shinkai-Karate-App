package be.mauricedeke.shinkai.ui.kaart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun KaartScreen(
    uiState: KaartUiState,
    onNavigateToEvent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.55f)
                    .height(200.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        RoundedCornerShape(4.dp)
                    )
            )
            Icon(
                Icons.Default.Map,
                null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.Center)
            )
            Icon(
                Icons.Default.LocationOn,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(40.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.height(20.dp)
                    )
                    Text(
                        "Shinkai Kempo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Karate Beernem",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Recently viewed",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (uiState.showEventDetail) {
            Button(
                onClick = onNavigateToEvent,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50)
            ) { Text("Navigeer Naar Event", fontSize = 16.sp, fontWeight = FontWeight.Medium) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun KaartScreenPreview() {
    ShinkaikarateappTheme { KaartScreen(uiState = KaartUiState()) }
}
