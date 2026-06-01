package be.mauricedeke.shinkai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

private data class DrawerItem(val label: String, val icon: ImageVector, val route: String)

private val drawerItems = listOf(
    DrawerItem("Kaart", Icons.Default.Map, "kaart"),
    DrawerItem("Technieken", Icons.AutoMirrored.Filled.DirectionsWalk, "technieken"),
    DrawerItem("Evenementen", Icons.Default.Event, "events"),
    DrawerItem("Home", Icons.Default.Home, "home"),
    DrawerItem("Lexicon", Icons.AutoMirrored.Filled.MenuBook, "lexicon"),
    DrawerItem("Profiel", Icons.Default.AccountCircle, "profiel"),
)

@Composable
fun ShinkaiDrawerContent(
    onClose: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(240.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.weight(1f))
            // Logo placeholder
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Shinkai Logo",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.width(8.dp))
        }

        Spacer(Modifier.height(16.dp))

        drawerItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate(item.route)
                        onClose()
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = item.label,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShinkaiDrawerContentPreview() {
    ShinkaikarateappTheme {
        ShinkaiDrawerContent()
    }
}
