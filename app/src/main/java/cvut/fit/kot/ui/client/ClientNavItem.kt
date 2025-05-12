package cvut.fit.kot.ui.client

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ClientNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Search  : ClientNavItem("search",  Icons.Outlined.Search,  "Search")
    object Orders  : ClientNavItem("orders",  Icons.Outlined.List, "Orders")
    object Chats   : ClientNavItem("chats",   Icons.Outlined.MailOutline, "Chats")
    object Profile : ClientNavItem("profile", Icons.Outlined.Person,  "Person")
}