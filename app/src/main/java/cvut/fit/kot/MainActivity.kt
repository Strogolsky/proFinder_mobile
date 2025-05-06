package cvut.fit.kot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import cvut.fit.kot.ui.auth.SignUpScreen
import cvut.fit.kot.ui.auth.SignUpViewModel
import cvut.fit.kot.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier) { innerPadding ->
                    SignUpScreen(
                        onSignUp = { email, password ->
                            signUpViewModel.signUp(email, password)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
