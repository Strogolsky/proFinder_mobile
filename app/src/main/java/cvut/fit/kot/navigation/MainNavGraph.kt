package cvut.fit.kot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cvut.fit.kot.ui.screen.ChatScreen
import cvut.fit.kot.ui.screen.AuthLandingScreen
import cvut.fit.kot.ui.screen.ChangeEmailScreen
import cvut.fit.kot.ui.screen.ChangePasswordScreen
import cvut.fit.kot.ui.screen.ClientMainScreen
import cvut.fit.kot.ui.screen.CreateOrderScreen
import cvut.fit.kot.ui.screen.EditOrderScreen
import cvut.fit.kot.ui.screen.EditProfileScreen
import cvut.fit.kot.ui.screen.ForgotPasswordScreen
import cvut.fit.kot.ui.screen.OrderDetailsScreen
import cvut.fit.kot.ui.screen.ResetPasswordScreen
import cvut.fit.kot.ui.screen.SignInScreen
import cvut.fit.kot.ui.screen.SignUpScreen
import cvut.fit.kot.ui.screen.SpecialistScreen
import cvut.fit.kot.ui.viewModel.AuthViewModel

@Composable
fun MainNavGraph(
    rootNav: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    NavHost(rootNav, startDestination = "landing") {

        composable("landing") {
            AuthLandingScreen(
                onSignUpClick = { rootNav.navigate("signup") },
                onSignInClick = { rootNav.navigate("signin") }
            )
        }

        composable("signup") {
            val uiState by viewModel.state.collectAsState()

            if (uiState is AuthViewModel.UiState.Success) {
                LaunchedEffect(Unit) {
                    rootNav.navigate("auth-observer") {
                        popUpTo("signup") { inclusive = true }
                    }
                    viewModel.resetState()
                }
            }

            SignUpScreen(
                uiState      = uiState,
                onSignUp     = { e, p, r -> viewModel.signUp(e, p, r) },
                onErrorShown = { viewModel.clearError() },
                onBack       = { rootNav.popBackStack() }
            )
        }

        composable("signin") {
            val uiState by viewModel.state.collectAsState()

            if (uiState is AuthViewModel.UiState.Success) {
                LaunchedEffect(Unit) {
                    rootNav.navigate("auth-observer") {
                        popUpTo("signin") { inclusive = true }
                    }
                    viewModel.resetState()
                }
            }

            SignInScreen(
                nav          = rootNav,
                uiState      = uiState,
                onSignIn     = { e, p, r -> viewModel.signIn(e, p, r) },
                onErrorShown = { viewModel.clearError() },
                onBack       = { rootNav.popBackStack() }
            )
        }

        composable("auth-observer") {
            val token by viewModel.tokenFlow.collectAsState(initial = null)
            val role  by viewModel.roleFlow.collectAsState(initial = null)

            LaunchedEffect(token, role) {
                if (!token.isNullOrEmpty() && role != null) {
                    val dest = if (role == "CLIENT") "client_main" else "specialist_main"
                    rootNav.navigate(dest) {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            }
        }

        composable("client_main")     { ClientMainScreen(rootNav) }
        composable("edit_profile")    { EditProfileScreen(rootNav) }
        composable("change_password") { ChangePasswordScreen(rootNav) }
        composable("forgot_password") { ForgotPasswordScreen(rootNav) }
        composable("change_email")    { ChangeEmailScreen(rootNav) }
        composable("create_order")    { CreateOrderScreen(rootNav)}

        composable(
            route     = "specialist/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { SpecialistScreen(rootNav) }

        composable(
            route     = "order/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getLong("orderId")
            OrderDetailsScreen(orderId = id, rootNav = rootNav)
        }

        composable(
            "chat/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.LongType })
        ) { ChatScreen(rootNav) }

        composable(
            route     = "order/{orderId}/edit",
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getLong("orderId")
            EditOrderScreen(orderId = id, rootNav = rootNav)
        }

        composable(
            route = "reset_password?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { ResetPasswordScreen(rootNav) }
    }
}
