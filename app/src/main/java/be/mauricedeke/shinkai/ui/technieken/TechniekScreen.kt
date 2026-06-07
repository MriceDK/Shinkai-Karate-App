package be.mauricedeke.shinkai.ui.technieken

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme
import be.mauricedeke.shinkai.ui.theme.toColor

@Composable
fun TechniekScreen(
    uiState: TechniekUiState,
    onBeltClick: (String) -> Unit = {},
    onKataClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (uiState.isError) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Kon technieken niet laden. Probeer opnieuw.",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier.padding(32.dp)
            )
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentPadding = PaddingValues(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.belts) { belt ->
            BeltCard(belt = belt, onClick = { onBeltClick(belt.name) })
        }
        item {
            KataSquare(onClick = onKataClick)
        }
    }
}

@Composable
private fun KataSquare(onClick: () -> Unit) {
    val colors = listOf(BeltColor.YELLOW, BeltColor.GREEN, BeltColor.BLUE, BeltColor.BROWN_I, BeltColor.BLACK)
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .shadow(
                8.dp,
                RoundedCornerShape(8.dp),
                ambientColor = MaterialTheme.colorScheme.outline,
                spotColor = MaterialTheme.colorScheme.outline
            )
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                colors.forEach { beltColor ->
                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(beltColor.toColor(isDark))
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Kata's", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun BeltCard(belt: Belt, onClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val beltColor = belt.beltColor.toColor(isDark)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .shadow(
                8.dp,
                RoundedCornerShape(8.dp),
                ambientColor = MaterialTheme.colorScheme.outline,
                spotColor = MaterialTheme.colorScheme.outline
            )
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(contentAlignment = Alignment.Center) {
                Box(modifier = Modifier
                    .size(width = 80.dp, height = 26.dp)
                    .clip(RoundedCornerShape(50))
                    .background(beltColor))
                Box(modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(beltColor.copy(alpha = 0.7f))
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        RoundedCornerShape(4.dp)
                    ))
            }
            Spacer(Modifier.height(12.dp))
            Text(belt.name, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

private val previewBelts = listOf(
    Belt(name = "Wit", beltColor = BeltColor.WHITE),
    Belt(name = "Geel", beltColor = BeltColor.YELLOW),
    Belt(name = "Oranje", beltColor = BeltColor.ORANGE),
    Belt(name = "Rood", beltColor = BeltColor.RED),
    Belt(name = "Groen", beltColor = BeltColor.GREEN),
    Belt(name = "Blauw", beltColor = BeltColor.BLUE),
    Belt(name = "Paars", beltColor = BeltColor.PURPLE),
    Belt(name = "Bruin I", beltColor = BeltColor.BROWN_I),
    Belt(name = "Zwart", beltColor = BeltColor.BLACK),
)

@Preview(name = "Technieken", showBackground = true, showSystemUi = true)
@Composable
fun TechniekScreenPreview() {
    ShinkaikarateappTheme { TechniekScreen(uiState = TechniekUiState(belts = previewBelts)) }
}

@Preview(name = "Technieken Dark", showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TechniekScreenDarkPreview() {
    ShinkaikarateappTheme(darkTheme = true) { TechniekScreen(uiState = TechniekUiState(belts = previewBelts)) }
}
