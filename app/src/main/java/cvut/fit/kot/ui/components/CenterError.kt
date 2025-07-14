package cvut.fit.kot.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenterError(pad: PaddingValues, msg: String) =
    Box(Modifier.fillMaxSize().padding(pad), Alignment.Center) { Text("Error: $msg") }