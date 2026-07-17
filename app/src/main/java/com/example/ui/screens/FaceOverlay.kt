package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.analyzer.FaceAnalysisResult
import com.example.domain.matcher.MatchResult

@Composable
fun FaceOverlay(
    result: FaceAnalysisResult?,
    match: MatchResult?,
    isFrontCamera: Boolean = true,
    modifier: Modifier = Modifier
) {
    if (result == null) return

    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val viewWidthPx = with(density) { maxWidth.toPx() }
        val viewHeightPx = with(density) { maxHeight.toPx() }

        val imageWidth = if (result.rotationDegrees == 90 || result.rotationDegrees == 270) result.imageHeight else result.imageWidth
        val imageHeight = if (result.rotationDegrees == 90 || result.rotationDegrees == 270) result.imageWidth else result.imageHeight

        if (imageWidth == 0 || imageHeight == 0) return@BoxWithConstraints

        val scaleX = viewWidthPx / imageWidth
        val scaleY = viewHeightPx / imageHeight
        val scale = maxOf(scaleX, scaleY)

        val scaledImageWidth = imageWidth * scale
        val scaledImageHeight = imageHeight * scale

        val offsetX = (viewWidthPx - scaledImageWidth) / 2f
        val offsetY = (viewHeightPx - scaledImageHeight) / 2f

        // Front camera preview is usually mirrored, back camera is not
        val leftPx = if (isFrontCamera) {
            viewWidthPx - (result.boundingBox.right * scale + offsetX)
        } else {
            result.boundingBox.left * scale + offsetX
        }
        val topPx = result.boundingBox.top * scale + offsetY
        val rightPx = if (isFrontCamera) {
            viewWidthPx - (result.boundingBox.left * scale + offsetX)
        } else {
            result.boundingBox.right * scale + offsetX
        }
        val bottomPx = result.boundingBox.bottom * scale + offsetY

        val widthPx = rightPx - leftPx
        val heightPx = bottomPx - topPx
        
        val leftDp = with(density) { leftPx.toDp() }
        val topDp = with(density) { topPx.toDp() }
        val widthDp = with(density) { widthPx.toDp() }
        val bottomDp = with(density) { bottomPx.toDp() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFFD4AF37).copy(alpha = pulseAlpha),
                topLeft = Offset(leftPx, topPx),
                size = Size(widthPx, heightPx),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                style = Stroke(width = 3.dp.toPx())
            )
        }

        if (match != null) {
            Box(
                modifier = Modifier
                    .offset(x = leftDp, y = bottomDp + 8.dp)
                    .width(widthDp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("YOU", color = Color.White, style = MaterialTheme.typography.labelSmall)
                    Text("Closest Design Match", color = Color(0xFFD4AF37), style = MaterialTheme.typography.labelMedium)
                    Text(match.character.name, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text("${match.similarityPercentage}% Similar", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
