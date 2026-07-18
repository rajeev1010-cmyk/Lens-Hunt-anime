package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.gianthunt.lenshunt.R

@Composable
fun SelfieOverlay(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.selfie_screen),
        contentDescription = "Selfie Interface Overlay",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}
