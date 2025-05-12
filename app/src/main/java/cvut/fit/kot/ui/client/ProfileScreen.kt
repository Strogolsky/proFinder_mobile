package cvut.fit.kot.ui.client

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(rootNav: NavHostController) {
    val ui = rememberFakeUser()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { /* edit */ }) {
                        Icon(Icons.Outlined.Edit, null)
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AsyncImage(
                model = ui.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(112.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
            )

            Text(ui.name, style = MaterialTheme.typography.headlineSmall)
            Text("📍 ${ui.city}", style = MaterialTheme.typography.bodyMedium)

            AssistChip(
                onClick = { },
                label = { Text(ui.role) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Contacts", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("Email:  ${ui.email}")
                    Text("Phone:  ${ui.phone}")
                }
            }
        }
    }
}

private data class FakeUser(
    val avatar: String,
    val name: String,
    val city: String,
    val role: String,
    val email: String,
    val phone: String
)

@Composable
private fun rememberFakeUser() = FakeUser(
    avatar = "https://i.pravatar.cc/256",
    name   = "Steve Smith",
    city   = "Prague",
    role   = "CLIENT",
    email  = "steve@mail.com",
    phone  = "+420 123 456"
)

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ProfilePreview() {
    ProfileScreen(rootNav = rememberNavController())
}
