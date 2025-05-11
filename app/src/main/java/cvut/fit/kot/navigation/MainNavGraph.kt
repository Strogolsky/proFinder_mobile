package cvut.fit.kot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cvut.fit.kot.ui.auth.AuthLandingScreen
import cvut.fit.kot.ui.auth.SignInScreen
import cvut.fit.kot.ui.auth.SignUpScreen

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "landing") {

        composable("landing") {
            AuthLandingScreen(
                onSignUpClick = { navController.navigate("signup") },
                onSignInClick = { navController.navigate("signin")}
            )
        }

        composable("signup") {
            SignUpScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("signin") {
            SignInScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}


