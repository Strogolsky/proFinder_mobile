package cvut.fit.kot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cvut.fit.kot.navigation.MainNavGraph
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainNavGraph() }
    }
}

