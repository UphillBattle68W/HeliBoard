package helium314.keyboard.latin.utils
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
object WallpaperBlurHelper {
private var cachedBlur: Drawable? = null
fun getBlurredWallpaperDrawable(context: Context, width: Int, height: Int): Drawable? {
if (width <= 0 || height <= 0) return null
if (cachedBlur != null) return cachedBlur
return try {
val wallpaperManager = WallpaperManager.getInstance(context)
val wallpaperDrawable = wallpaperManager.drawable ?: return null
val fullBitmap = wallpaperDrawable.toBitmap()
val cropY = (fullBitmap.height - height).coerceAtLeast(0)
val cropped = Bitmap.createBitmap(
fullBitmap,
0,
cropY,
fullBitmap.width.coerceAtMost(width),
height.coerceAtMost(fullBitmap.height)
)
val downsampled = Bitmap.createScaledBitmap(
cropped,
(cropped.width / 4).coerceAtLeast(1),
(cropped.height / 4).coerceAtLeast(1),
true
)
val canvas = Canvas(downsampled)
val tintPaint = Paint().apply {
color = Color.argb(140, 18, 18, 22)
}
canvas.drawRect(0f, 0f, downsampled.width.toFloat(), downsampled.height.toFloat(), tintPaint)
val finalDrawable = BitmapDrawable(context.resources, downsampled)
cachedBlur = finalDrawable
finalDrawable
} catch (e: Exception) {
null
}
}
fun clearCache() {
cachedBlur = null
}
}
