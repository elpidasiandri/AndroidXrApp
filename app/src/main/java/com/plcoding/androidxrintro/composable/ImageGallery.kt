package com.plcoding.androidxrintro.composable

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.plcoding.androidxrintro.R
import kotlinx.coroutines.launch
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.ImageLoader
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue

@Composable
fun ImageGallery(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    val gifPainter = rememberAsyncImagePainter(
        model = "https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExMGh1cHF1NGhwa3E2dnU0YTh0dmd3ZzBqb2ZqY2Y3bHkzd3IzNHZpNiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/98uBZTzlXMhkk/giphy.gif",
        imageLoader = imageLoader
    )

    val pictures = listOf(
        R.drawable.android_person,
        R.drawable.activity_lifecycle_info,
        R.drawable.on_create,
        R.drawable.on_start,
        R.drawable.on_resume,
        R.drawable.on_pause,
        R.drawable.on_stop,
        R.drawable.on_restart,
        R.drawable.on_destroy,
        R.drawable.best_practices,
        R.drawable.android_robot,
    )

    val pagerState = rememberPagerState { pictures.size }
    val scope = rememberCoroutineScope()
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        val soundRes = when (pictures[pagerState.currentPage]) {
            R.drawable.activity_lifecycle_info -> R.raw.intro
            R.drawable.on_create -> R.raw.oncreate
            R.drawable.on_start -> R.raw.onstart
            R.drawable.on_resume -> R.raw.onresume
            R.drawable.on_pause -> R.raw.onpause
            R.drawable.on_stop -> R.raw.onstop
            R.drawable.on_restart -> R.raw.onrestart
            R.drawable.on_destroy -> R.raw.ondestroy
            R.drawable.best_practices -> R.raw.practice
            else -> null
        }

        soundRes?.let { resId ->
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                }
                start()
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = gifPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .width(290.dp)
                    .height(350.dp)
                    .align(Alignment.Center)
            ) { page ->
                Image(
                    painter = painterResource(pictures[page]),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = pagerState.currentPage > 0,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = "Previous"
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                enabled = pagerState.currentPage < pictures.lastIndex,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "Next"
                )
            }
        }
    }
}
