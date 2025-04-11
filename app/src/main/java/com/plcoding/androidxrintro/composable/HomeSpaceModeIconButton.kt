package com.plcoding.androidxrintro.composable

import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.plcoding.androidxrintro.R
import com.plcoding.androidxrintro.ui.theme.AndroidXRIntroTheme


@Composable
fun HomeSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_home_space_mode)
        )
    }
}
@PreviewLightDark
@Composable
fun HomeSpaceModeButtonPreview() {
    AndroidXRIntroTheme {
        HomeSpaceModeIconButton(onClick = {})
    }
}