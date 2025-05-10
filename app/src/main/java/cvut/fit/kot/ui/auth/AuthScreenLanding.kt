package cvut.fit.kot.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cvut.fit.kot.ui.theme.MyApplicationTheme

@Composable
fun AuthLandingScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Sign In")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AuthLandingScreenPreview() {
    MyApplicationTheme {
        AuthLandingScreen(
            onSignUpClick = {},
            onSignInClick = {}
        )
    }
}
