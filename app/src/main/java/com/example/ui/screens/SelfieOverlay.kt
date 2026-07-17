package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SentimentSatisfied

val GiantGold = Color(0xFFECA72C)

@Composable
fun SelfieOverlay(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background and Frame
        Canvas(modifier = Modifier.fillMaxSize()) {
            val horizontalPadding = 24.dp.toPx()
            val topPadding = 280.dp.toPx()
            val bottomPadding = 180.dp.toPx()
            
            val frameWidth = size.width - (horizontalPadding * 2)
            val frameHeight = size.height - topPadding - bottomPadding
            
            val offsetX = horizontalPadding
            val offsetY = topPadding
            val cutSize = 24.dp.toPx()
            
            val innerPath = Path().apply {
                moveTo(offsetX + cutSize, offsetY)
                lineTo(offsetX + frameWidth - cutSize, offsetY)
                lineTo(offsetX + frameWidth, offsetY + cutSize)
                lineTo(offsetX + frameWidth, offsetY + frameHeight - cutSize)
                lineTo(offsetX + frameWidth - cutSize, offsetY + frameHeight)
                lineTo(offsetX + cutSize, offsetY + frameHeight)
                lineTo(offsetX, offsetY + frameHeight - cutSize)
                lineTo(offsetX, offsetY + cutSize)
                close()
            }
            
            val bgPath = Path().apply {
                addRect(Rect(0f, 0f, size.width, size.height))
                addPath(innerPath)
                fillType = PathFillType.EvenOdd
            }
            
            // Draw solid black background
            drawPath(bgPath, color = Color.Black)
            
            // Draw the golden frame border
            drawPath(innerPath, color = GiantGold, style = Stroke(width = 2.dp.toPx()))
            
            // Center X for decorations
            val cx = offsetX + frameWidth / 2
            
            // Top circles and star
            drawCircle(color = GiantGold, radius = 24.dp.toPx(), center = Offset(cx, offsetY), style = Stroke(width = 1.dp.toPx()))
            drawCircle(color = GiantGold, radius = 32.dp.toPx(), center = Offset(cx, offsetY), style = Stroke(width = 1.dp.toPx()))
            drawCircle(color = GiantGold, radius = 48.dp.toPx(), center = Offset(cx, offsetY), style = Stroke(width = 1.dp.toPx()))
            
            val starPathTop = Path().apply {
                moveTo(cx, offsetY - 16.dp.toPx())
                quadraticBezierTo(cx, offsetY, cx + 16.dp.toPx(), offsetY)
                quadraticBezierTo(cx, offsetY, cx, offsetY + 16.dp.toPx())
                quadraticBezierTo(cx, offsetY, cx - 16.dp.toPx(), offsetY)
                quadraticBezierTo(cx, offsetY, cx, offsetY - 16.dp.toPx())
                close()
            }
            drawPath(starPathTop, color = GiantGold)
            
            // Bottom circles and star
            val bottomY = offsetY + frameHeight
            drawCircle(color = GiantGold, radius = 24.dp.toPx(), center = Offset(cx, bottomY), style = Stroke(width = 1.dp.toPx()))
            drawCircle(color = GiantGold, radius = 32.dp.toPx(), center = Offset(cx, bottomY), style = Stroke(width = 1.dp.toPx()))
            drawCircle(color = GiantGold, radius = 48.dp.toPx(), center = Offset(cx, bottomY), style = Stroke(width = 1.dp.toPx()))

            val starPathBottom = Path().apply {
                moveTo(cx, bottomY - 16.dp.toPx())
                quadraticBezierTo(cx, bottomY, cx + 16.dp.toPx(), bottomY)
                quadraticBezierTo(cx, bottomY, cx, bottomY + 16.dp.toPx())
                quadraticBezierTo(cx, bottomY, cx - 16.dp.toPx(), bottomY)
                quadraticBezierTo(cx, bottomY, cx, bottomY - 16.dp.toPx())
                close()
            }
            drawPath(starPathBottom, color = GiantGold)
            
            // Side stars
            val cy = offsetY + frameHeight / 2
            val sideStarPathL = Path().apply {
                moveTo(offsetX, cy - 12.dp.toPx())
                quadraticBezierTo(offsetX, cy, offsetX + 12.dp.toPx(), cy)
                quadraticBezierTo(offsetX, cy, offsetX, cy + 12.dp.toPx())
                quadraticBezierTo(offsetX, cy, offsetX - 12.dp.toPx(), cy)
                quadraticBezierTo(offsetX, cy, offsetX, cy - 12.dp.toPx())
                close()
            }
            drawPath(sideStarPathL, color = GiantGold)
            
            val sideStarPathR = Path().apply {
                moveTo(offsetX + frameWidth, cy - 12.dp.toPx())
                quadraticBezierTo(offsetX + frameWidth, cy, offsetX + frameWidth + 12.dp.toPx(), cy)
                quadraticBezierTo(offsetX + frameWidth, cy, offsetX + frameWidth, cy + 12.dp.toPx())
                quadraticBezierTo(offsetX + frameWidth, cy, offsetX + frameWidth - 12.dp.toPx(), cy)
                quadraticBezierTo(offsetX + frameWidth, cy, offsetX + frameWidth, cy - 12.dp.toPx())
                close()
            }
            drawPath(sideStarPathR, color = GiantGold)
        }

        // Top Content
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: THE GIANT HUNT
                Column {
                    Text("THE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("GIANT", color = GiantGold, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text("HUNT", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 24.dp))
                }
                
                // Center: GWR Logo Placeholder
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(color = Color.White, style = Stroke(width = 2.dp.toPx()))
                        drawCircle(color = Color.White, radius = size.minDimension / 2 - 8.dp.toPx(), style = Stroke(width = 1.dp.toPx()))
                    }
                    Text("GWR", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Right: OFFICIAL ATTEMPT
                Column(horizontalAlignment = Alignment.End) {
                    Text("OFFICIAL", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("ATTEMPT", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // FIND YOUR ANIME TWIN
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.width(30.dp).height(2.dp)) {
                    drawLine(color = GiantGold, start = Offset(0f, size.height/2), end = Offset(size.width, size.height/2), strokeWidth = 2.dp.toPx())
                    // small diamond
                    val diamond = Path().apply {
                        moveTo(size.width, size.height/2 - 4.dp.toPx())
                        lineTo(size.width + 4.dp.toPx(), size.height/2)
                        lineTo(size.width, size.height/2 + 4.dp.toPx())
                        lineTo(size.width - 4.dp.toPx(), size.height/2)
                        close()
                    }
                    drawPath(diamond, GiantGold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "FIND YOUR\nANIME TWIN",
                    color = GiantGold,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Canvas(modifier = Modifier.width(30.dp).height(2.dp)) {
                    drawLine(color = GiantGold, start = Offset(0f, size.height/2), end = Offset(size.width, size.height/2), strokeWidth = 2.dp.toPx())
                    // small diamond
                    val diamond = Path().apply {
                        moveTo(0f, size.height/2 - 4.dp.toPx())
                        lineTo(4.dp.toPx(), size.height/2)
                        lineTo(0f, size.height/2 + 4.dp.toPx())
                        lineTo(-4.dp.toPx(), size.height/2)
                        close()
                    }
                    drawPath(diamond, GiantGold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("TAKE YOUR SELFIE", color = GiantGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("STEP 1 / 2", color = GiantGold, fontSize = 14.sp)
        }

        // Bottom Content
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.width(60.dp).height(1.dp)) {
                    drawLine(color = GiantGold, start = Offset(0f, 0f), end = Offset(size.width, 0f), strokeWidth = 1.dp.toPx())
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("SELFIE TIPS", color = GiantGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(16.dp))
                Canvas(modifier = Modifier.width(60.dp).height(1.dp)) {
                    drawLine(color = GiantGold, start = Offset(0f, 0f), end = Offset(size.width, 0f), strokeWidth = 1.dp.toPx())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tips Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cut = 12.dp.toPx()
                    val p = Path().apply {
                        moveTo(cut, 0f)
                        lineTo(size.width - cut, 0f)
                        lineTo(size.width, cut)
                        lineTo(size.width, size.height - cut)
                        lineTo(size.width - cut, size.height)
                        lineTo(cut, size.height)
                        lineTo(0f, size.height - cut)
                        lineTo(0f, cut)
                        close()
                    }
                    drawPath(p, color = GiantGold, style = Stroke(width = 1.dp.toPx()))
                    
                    // Separators
                    drawLine(color = GiantGold, start = Offset(size.width/3, 16.dp.toPx()), end = Offset(size.width/3, size.height - 16.dp.toPx()))
                    drawLine(color = GiantGold, start = Offset(size.width*2/3, 16.dp.toPx()), end = Offset(size.width*2/3, size.height - 16.dp.toPx()))
                }
                
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tip 1
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Face, contentDescription = null, tint = GiantGold, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Face\nthe camera", color = Color.White, fontSize = 12.sp, lineHeight = 14.sp)
                    }
                    // Tip 2
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.LightMode, contentDescription = null, tint = GiantGold, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Use\ngood lighting", color = Color.White, fontSize = 12.sp, lineHeight = 14.sp)
                    }
                    // Tip 3
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.SentimentSatisfied, contentDescription = null, tint = GiantGold, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Natural\nexpression", color = Color.White, fontSize = 12.sp, lineHeight = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
