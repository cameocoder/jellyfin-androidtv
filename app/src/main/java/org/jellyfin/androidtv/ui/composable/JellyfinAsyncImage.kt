package org.jellyfin.androidtv.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.util.BlurHashDecoder
import kotlin.math.round

@Composable
fun JellyfinAsyncImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    contentDescription: String? = null,
    blurHash: String? = null,
    @DrawableRes placeholderRes: Int? = null,
    aspectRatio: Float = 1f,
    blurHashResolution: Int = 32,
    scaleType: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    var placeholderOrBlurHash: BitmapPainter? by remember { mutableStateOf(null) }

    if (blurHash != null) {
        val blurHashBitmap = BlurHashDecoder.decode(
            blurHash = blurHash,
            width = if (aspectRatio > 1) round(blurHashResolution * aspectRatio).toInt() else blurHashResolution,
            height = if (aspectRatio >= 1) blurHashResolution else round(blurHashResolution / aspectRatio).toInt(),
        )
        if (blurHashBitmap != null) {
            placeholderOrBlurHash = BitmapPainter(blurHashBitmap.asImageBitmap())
        }
    }
    if (placeholderOrBlurHash == null) {
        val placeholder = placeholderRes?.let { ContextCompat.getDrawable(context, it) }
        placeholderOrBlurHash = placeholder?.let { BitmapPainter(it.toBitmap().asImageBitmap()) }
    }
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        placeholder = placeholderOrBlurHash,
        fallback = placeholderOrBlurHash,
        error = placeholderOrBlurHash,
        contentScale = scaleType,
        modifier = modifier.aspectRatio(aspectRatio)
    )
}

/**
 * A preview of the [JellyfinAsyncImage] composable.  Images will only load when preview is run
 * on a real device.
 */
@Preview(device = Devices.TV_1080p)
@Composable
private fun JellyfinAsyncImagePreview() {
    val placeholderRes = R.drawable.ic_movie
    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row {
            // Load URL on real device or show blurhash in preview)
            JellyfinAsyncImage(
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(5.dp)),
                url = "https://demo.jellyfin.org/stable/Items/b07f6514-a3e3-aa7d-8c27-3458c9cd4c82/Images/Primary?tag=29526c6e8f8d9286c8f6d03275da6ebd",
                blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
                placeholderRes = placeholderRes,
                aspectRatio = 0.66f,
                blurHashResolution = 16,
            )
            // Circle
            JellyfinAsyncImage(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                url = "https://demo.jellyfin.org/stable/Items/b07f6514-a3e3-aa7d-8c27-3458c9cd4c82/Images/Primary?tag=29526c6e8f8d9286c8f6d03275da6ebd",
                blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
                aspectRatio = 1f,
            )
        }

        Row {
            // show blurhash since url is corrupt
            JellyfinAsyncImage(
                modifier = Modifier
                    .size(128.dp),
                url = "82/Images/Primary?tag=29526c6e8f8d9286c8f6d03275da6ebd",
                blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
                aspectRatio = 0.66f,
            )
            // Show placeholder since url and blurhash is corrupt
            JellyfinAsyncImage(
                modifier = Modifier
                    .size(128.dp),
                url = "",
                blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oe",
                placeholderRes = placeholderRes,
                aspectRatio = 0.66f,
            )
        }
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun BlurHashResolutionPreview() {
    Row(Modifier.padding(16.dp)) {
        JellyfinAsyncImage(
            modifier = Modifier
                .size(128.dp),
            blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
            aspectRatio = 0.66f,
            blurHashResolution = 8,
        )
        JellyfinAsyncImage(
            modifier = Modifier
                .size(128.dp),
            blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
            aspectRatio = 0.66f,
            blurHashResolution = 16,
        )
        JellyfinAsyncImage(
            modifier = Modifier
                .size(128.dp),
            blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
            aspectRatio = 0.66f,
            blurHashResolution = 32,
        )
        JellyfinAsyncImage(
            modifier = Modifier
                .size(128.dp),
            blurHash = "dI9Qjlof00RjxukBRjM{ITaz-;j[WBj[t7oeIUaxt7WB",
            aspectRatio = 0.66f,
            blurHashResolution = 128,
        )
    }
}
