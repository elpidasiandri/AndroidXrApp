package com.plcoding.androidxrintro.composable.threeD

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.plcoding.androidxrintro.R
import coil.decode.GifDecoder
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun SlidesGallery(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(GifDecoder.Factory()) }
            .build()
    }

    val pictures = listOf(
        "https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExMGh1cHF1NGhwa3E2dnU0YTh0dmd3ZzBqb2ZqY2Y3bHkzd3IzNHZpNiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/98uBZTzlXMhkk/giphy.gif",
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

    SlideSoundPlayer(pagerState = pagerState, pictures = pictures)

    Box(
        modifier = modifier
            .size(550.dp)
            .background(Color.Black)
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            SlidesPager(
                pictures = pictures,
                pagerState = pagerState,
                imageLoader = imageLoader
            )

            SlideNavigationButtons(pagerState = pagerState)
        }
    }
}


@Composable
fun SlidesPager(
    pictures: List<Any>,
    pagerState: PagerState,
    imageLoader: ImageLoader
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .width(400.dp)
            .height(550.dp)
    ) { page ->
        val current = pictures[page]
        if (current is String) {
            Image(
                painter = rememberAsyncImagePainter(model = current, imageLoader = imageLoader),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (current is Int) {
            Image(
                painter = painterResource(current),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun BoxScope.SlideNavigationButtons(pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        },
        enabled = pagerState.currentPage > 0,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
            .zIndex(1f)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous"
        )
    }

    IconButton(
        onClick = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
        enabled = pagerState.currentPage < pagerState.pageCount - 1,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .zIndex(1f)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next"
        )
    }
}

@Composable
fun SlideSoundPlayer(
    pagerState: PagerState,
    pictures: List<Any>
) {
    val context = LocalContext.current
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

        val current = pictures[pagerState.currentPage]
        val soundRes = when (current) {
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
}



