package be.mauricedeke.shinkai.ui.technieken

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.domain.model.Belt
import be.mauricedeke.shinkai.ui.theme.BeltBlue
import be.mauricedeke.shinkai.ui.theme.BeltGreen
import be.mauricedeke.shinkai.ui.theme.BeltOrange
import be.mauricedeke.shinkai.ui.theme.BeltRed
import be.mauricedeke.shinkai.ui.theme.BeltYellow
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun TechniekScreen(
    uiState: TechniekUiState,
    onBeltClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val belts = uiState.belts.ifEmpty {
        listOf(
            Belt("Geel", "#FFD700"), Belt("Oranje", "#FF8C00"),
            Belt("Rood", "#CC0000"), Belt("Groen", "#1B5E20"),
            Belt("Blauw", "#1565C0"),
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 24.dp),
        contentPadding = PaddingValues(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(belts) { belt ->
            BeltCard(belt = belt, onClick = { onBeltClick(belt.name) })
        }
    }
}

@Composable
private fun BeltCard(belt: Belt, onClick: () -> Unit) {
    val beltColor = when (belt.name) {
        "Geel" -> BeltYellow
        "Oranje" -> BeltOrange
        "Rood" -> BeltRed
        "Groen" -> BeltGreen
        "Blauw" -> BeltBlue
        else -> BeltYellow
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = MaterialTheme.colorScheme.outline, spotColor = MaterialTheme.colorScheme.outline)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(width = 80.dp, height = 26.dp).clip(RoundedCornerShape(50)).background(beltColor))
                Box(modifier = Modifier.size(26.dp).clip(RoundedCornerShape(4.dp)).background(beltColor.copy(alpha = 0.7f)).border(2.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), RoundedCornerShape(4.dp)))
            }
            Spacer(Modifier.height(12.dp))
            Text(belt.name, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TechniekScreenPreview() {
    ShinkaikarateappTheme {
        TechniekScreen(uiState = TechniekUiState())
    }
}
