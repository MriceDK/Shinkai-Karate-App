package be.mauricedeke.shinkai.ui.profiel.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import be.mauricedeke.shinkai.ui.components.RoundBackButton
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mauricedeke.shinkai.ui.theme.ShinkaiRed
import be.mauricedeke.shinkai.ui.theme.ShinkaikarateappTheme

@Composable
fun AccountScreen(
    uiState: AccountUiState,
    onNaamChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onNewPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onSavePassword: () -> Unit = {},
    onSave: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(modifier = Modifier.size(96.dp).clip(CircleShape).background(ShinkaiRed), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(60.dp))
            }
            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Edit, "Edit avatar", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface)
            }
        }
        Spacer(Modifier.height(24.dp))
        FormField("NAAM:", uiState.naam, onNaamChanged)
        Spacer(Modifier.height(12.dp))
        FormField("EMAIL:", uiState.email, onEmailChanged, keyboardType = KeyboardType.Email)
        Spacer(Modifier.height(16.dp))
        FormField("New Password:", uiState.newPassword, onNewPasswordChanged, password = true)
        Spacer(Modifier.height(12.dp))
        FormField("Confirm Password:", uiState.confirmPassword, onConfirmPasswordChanged, password = true)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onSavePassword, colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed), shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("Save Password", fontSize = 15.sp)
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = onSave, colors = ButtonDefaults.buttonColors(containerColor = ShinkaiRed), shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth(0.5f).padding(bottom = 8.dp)) {
            Text("Save", fontSize = 16.sp)
        }
        Spacer(Modifier.height(16.dp))
    }
    RoundBackButton(
        onClick = onBackClick,
        modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = 8.dp)
    )
}
}

@Composable
private fun FormField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, password: Boolean = false) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountScreenPreview() {
    ShinkaikarateappTheme { AccountScreen(uiState = AccountUiState()) }
}
