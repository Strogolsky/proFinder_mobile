package cvut.fit.kot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon

/**
 * Common avatar with circular frame.
 * @param bitmap  user picture or *null* for placeholder
 * @param size    diameter of avatar (default 112 dp)
 */
@Composable
fun AvatarImage(
    bitmap: ImageBitmap?,
    modifier: Modifier = Modifier,
    size: Dp = 112.dp
) {
    val base = modifier
        .size(size)
        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        .padding(2.dp)
        .clip(CircleShape)

    if (bitmap != null) {
        Image(bitmap, null, base)
    } else {
        Icon(Icons.Default.AccountCircle, null, base)
    }
}
