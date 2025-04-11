package com.plcoding.androidxrintro.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.xr.compose.platform.LocalHasXrSpatialFeature
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.plcoding.androidxrintro.ui.theme.AndroidXRIntroTheme

@SuppressLint("RestrictedApi")
@Composable
fun My2DContent(onRequestFullSpaceMode: () -> Unit) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            ImageGallery(
                modifier = Modifier
                    .fillMaxSize()
            )
            if (LocalHasXrSpatialFeature.current) {
                FullSpaceModeIconButton(
                    onClick = onRequestFullSpaceMode,
                    modifier = Modifier
                        .padding(32.dp)
                        .align(Alignment.TopEnd),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun My2dContentPreview() {
    AndroidXRIntroTheme {
        My2DContent(onRequestFullSpaceMode = {})
    }
}
