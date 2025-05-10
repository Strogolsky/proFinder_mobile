package cvut.fit.kot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import cvut.fit.kot.ui.auth.AuthLandingScreen
import cvut.fit.kot.ui.auth.SignUpScreen
import cvut.fit.kot.ui.auth.SignUpViewModel
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "landing") {

        composable("landing") {
            AuthLandingScreen(
                onSignUpClick = { navController.navigate("signup") },
                onSignInClick = { }
            )
        }

        composable("signup") {
            SignUpScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}


