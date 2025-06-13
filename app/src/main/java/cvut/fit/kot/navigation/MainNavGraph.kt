package cvut.fit.kot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cvut.fit.kot.ui.auth.AuthLandingScreen
import cvut.fit.kot.ui.auth.AuthViewModel
import cvut.fit.kot.ui.auth.ChangePasswordScreen
import cvut.fit.kot.ui.auth.SignInScreen
import cvut.fit.kot.ui.auth.SignUpScreen
import cvut.fit.kot.ui.client.ClientMainScreen
import cvut.fit.kot.ui.client.EditProfileScreen

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
            SignUpScreen(
                uiState  = uiState,
                onSignUp = { e, p, r ->
                    viewModel.signUp(e, p, r)
                    rootNav.navigate("auth-observer") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBack   = { rootNav.popBackStack() }
            )
        }

        composable("signin") {
            val uiState by viewModel.state.collectAsState()
            SignInScreen(
                uiState  = uiState,
                onSignIn = { e, p, r ->
                    viewModel.signIn(e, p, r)
                    rootNav.navigate("auth-observer") {
                        popUpTo("signin") { inclusive = true }
                    }
                },
                onBack   = { rootNav.popBackStack() }
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

        composable("client_main") { ClientMainScreen(rootNav) }
        composable("edit_profile") { EditProfileScreen(rootNav) }
        composable("change_password") { ChangePasswordScreen(rootNav)}
    }
}
