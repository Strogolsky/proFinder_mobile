package cvut.fit.kot.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cvut.fit.kot.ui.components.ClientNavItem

@Composable
fun ClientMainScreen(rootNav: NavHostController) {

    val childNav = rememberNavController()
    val items = listOf(
        ClientNavItem.Search,
        ClientNavItem.Orders,
        ClientNavItem.Chats,
        ClientNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute =
                    childNav.currentBackStackEntryAsState().value?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            childNav.navigate(item.route) {
                                popUpTo(childNav.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) { inner ->
        NavHost(
            navController = childNav,
            startDestination = ClientNavItem.Search.route,
            modifier = Modifier.padding(inner)
        ) {
            composable(ClientNavItem.Search.route) { SearchScreen(rootNav) }
            composable(ClientNavItem.Orders.route) { OrderListScreen(rootNav) }
            composable(ClientNavItem.Chats.route) { ChatsScreen(rootNav) }
            composable(ClientNavItem.Profile.route) { ProfileScreen(rootNav) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientMainPreview() {
    ClientMainScreen(rootNav = rememberNavController())
}
