package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.domain.matcher.MatchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.ContextCompat
import com.gianthunt.lenshunt.R

object ShareCardGenerator {
    suspend fun generateAndShare(context: Context, selfie: Bitmap, matchResult: MatchResult) {
        val width = 941
        val height = 1672

        val finalBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        val goldColor = Color.parseColor("#ECA72C")
        val blackColor = Color.parseColor("#050505")
        
        // 1. Load background image from R.drawable.animetwin_sharecard
        val backgroundSrc = BitmapFactory.decodeResource(context.resources, R.drawable.animetwin_sharecard)
        if (backgroundSrc != null) {
            val scaledBg = Bitmap.createScaledBitmap(backgroundSrc, width, height, true)
            canvas.drawBitmap(scaledBg, 0f, 0f, null)
        } else {
            // Fallback: draw dark background, borders, logos, etc. like the original code did
            canvas.drawColor(blackColor)
            
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                style = Paint.Style.STROKE
                strokeWidth = 4f
            }
            val innerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                style = Paint.Style.STROKE
                strokeWidth = 1f
            }
            canvas.drawRect(24f, 24f, width - 24f, height - 24f, borderPaint)
            canvas.drawRect(36f, 36f, width - 36f, height - 36f, innerBorderPaint)

            // Corner squares
            val cornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColor; style = Paint.Style.FILL }
            canvas.drawRect(20f, 20f, 40f, 40f, cornerPaint)
            canvas.drawRect(width - 40f, 20f, width - 20f, 40f, cornerPaint)
            canvas.drawRect(20f, height - 40f, 40f, height - 20f, cornerPaint)
            canvas.drawRect(width - 40f, height - 40f, width - 20f, height - 20f, cornerPaint)

            // Title Area
            val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = 32f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.LEFT
            }
            
            // Left: Giant Hunt Logo
            val giantDrawable = ContextCompat.getDrawable(context, R.drawable.logo_giant_hunt)
            giantDrawable?.setBounds(80, 80, 80 + 360, 80 + 120)
            giantDrawable?.draw(canvas)
            
            canvas.drawText("OFFICIAL", width - 320f, 130f, Paint(headerPaint).apply { textSize = 32f })
            canvas.drawText("ATTEMPT", width - 320f, 170f, Paint(headerPaint).apply { textSize = 32f })
            
            // Divider lines
            canvas.drawLine(width / 2f + 120f, 100f, width / 2f + 120f, 180f, innerBorderPaint)
            canvas.drawLine(width / 2f - 120f, 100f, width / 2f - 120f, 180f, innerBorderPaint)
            
            // Center: GWR Logo
            val gwrDrawable = ContextCompat.getDrawable(context, R.drawable.logo_gwr)
            gwrDrawable?.setBounds((width / 2f - 80f).toInt(), 70, (width / 2f + 80f).toInt(), 210)
            gwrDrawable?.draw(canvas)

            val currentY = 280f
            val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColor; strokeWidth = 1.5f }
            canvas.drawLine(80f, currentY, 280f, currentY, dividerPaint)
            canvas.drawPath(Path().apply {
                moveTo(280f, currentY - 8f)
                lineTo(290f, currentY)
                lineTo(280f, currentY + 8f)
                close()
            }, cornerPaint)
            
            canvas.drawLine(width - 280f, currentY, width - 80f, currentY, dividerPaint)
            canvas.drawPath(Path().apply {
                moveTo(width - 280f, currentY - 8f)
                lineTo(width - 290f, currentY)
                lineTo(width - 280f, currentY + 8f)
                close()
            }, cornerPaint)
            
            val sectionTitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                textSize = 32f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                textAlign = Paint.Align.CENTER
                letterSpacing = 0.15f
            }
            canvas.drawText("ANIME TWIN MATCH", width / 2f, currentY + 12f, sectionTitlePaint)
        }

        // --- Common Drawing parameters ---
        val goldColorValue = Color.parseColor("#ECA72C")
        val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColorValue; strokeWidth = 1.5f }

        // 2. Selfie Drawing inside Left photo box
        // Coordinates: x: 40-334, y: 605-1084 (inner safe: 50-324, y: 615-1074)
        val leftBoxX = 50f
        val boxY = 615f
        val photoWidth = 274f
        val photoHeight = 459f
        val cornerCut = 20f

        val scaledSelfie = scaleAndCropCenter(selfie, photoWidth.toInt(), photoHeight.toInt())
        val photoPath = Path().apply {
            moveTo(leftBoxX + cornerCut, boxY)
            lineTo(leftBoxX + photoWidth - cornerCut, boxY)
            lineTo(leftBoxX + photoWidth, boxY + cornerCut)
            lineTo(leftBoxX + photoWidth, boxY + photoHeight - cornerCut)
            lineTo(leftBoxX + photoWidth - cornerCut, boxY + photoHeight)
            lineTo(leftBoxX + cornerCut, boxY + photoHeight)
            lineTo(leftBoxX, boxY + photoHeight - cornerCut)
            lineTo(leftBoxX, boxY + cornerCut)
            close()
        }
        
        canvas.save()
        canvas.clipPath(photoPath)
        canvas.drawBitmap(scaledSelfie, leftBoxX, boxY, null)
        canvas.restore()

        // 3. YOUR ANIME TWIN (Right photo box) Label overlay
        // Coordinates: x: 594-897, y: 605-1084 (inner safe: 605-887, y: 615-1074)
        val rightBoxX = 605f
        val rightPhotoWidth = 282f
        val charNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            textSize = 52f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create("cursive", Typeface.BOLD)
            setShadowLayer(16f, 0f, 6f, Color.BLACK)
        }
        
        // Draw the name in the center of the right box
        val rightBoxCenterY = boxY + (photoHeight / 2f)
        val textY = rightBoxCenterY + (charNamePaint.textSize / 3f)
        canvas.drawText(matchResult.character.name, rightBoxX + rightPhotoWidth / 2f, textY, charNamePaint)

        // 4. Center Rings - MATCH percentage info
        val centerCx = width / 2f
        val centerCyText = 692f
        
        val matchTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            textSize = 18f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.05f
        }
        canvas.drawText("MATCH", centerCx, centerCyText - 15f, matchTextPaint)
        
        val percentagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 48f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        canvas.drawText("${matchResult.similarityPercentage}%", centerCx, centerCyText + 35f, percentagePaint)

        // 5. Details Section (Dynamic Fields)
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
            letterSpacing = 0.05f
        }

        val bodyPaintBold = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 18f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            setShadowLayer(3f, 0f, 3f, Color.BLACK)
        }

        val bodyPaintTaller = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 15f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            setShadowLayer(3f, 0f, 3f, Color.BLACK)
        }

        val iconSize = 24f

        // Box 1: Creator / Designer (Row 1, Left)
        val box1Left = 40f
        val box1Top = 1092f
        
        drawBoxHeader(
            canvas, context, R.drawable.ic_designer, "CREATOR / DESIGNER",
            iconX = box1Left + 15f,
            iconY = box1Top + 15f,
            iconSize = iconSize,
            titleX = box1Left + 15f + iconSize + 10f,
            titleY = box1Top + 32f,
            titlePaint = titlePaint,
            goldColorValue = goldColorValue
        )
        canvas.drawText(
            matchResult.character.designer,
            box1Left + 15f + iconSize + 10f,
            box1Top + 65f,
            bodyPaintBold
        )

        // Box 2: Design Language (Row 1, Right)
        val box2Left = 475f
        val box2Top = 1092f
        
        drawBoxHeader(
            canvas, context, R.drawable.ic_language, "DESIGN LANGUAGE",
            iconX = box2Left + 15f,
            iconY = box2Top + 15f,
            iconSize = iconSize,
            titleX = box2Left + 15f + iconSize + 10f,
            titleY = box2Top + 32f,
            titlePaint = titlePaint,
            goldColorValue = goldColorValue
        )
        canvas.drawText(
            matchResult.character.designLanguage,
            box2Left + 15f + iconSize + 10f,
            box2Top + 65f,
            bodyPaintBold
        )

        // Box 3: Visual Traits (Row 2, Wide Span, Box 1)
        val box3Left = 40f
        val box3Top = 1193f
        
        drawBoxHeader(
            canvas, context, R.drawable.ic_visual, "VISUAL TRAITS",
            iconX = box3Left + 15f,
            iconY = box3Top + 15f,
            iconSize = iconSize,
            titleX = box3Left + 15f + iconSize + 10f,
            titleY = box3Top + 32f,
            titlePaint = titlePaint,
            goldColorValue = goldColorValue
        )
        val traitsTextStart = box3Left + 15f + iconSize + 10f
        val traitsLayout = StaticLayout.Builder.obtain(
            matchResult.character.visualTraits,
            0,
            matchResult.character.visualTraits.length,
            bodyPaintBold,
            (898f - traitsTextStart - 15f).toInt()
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(2)
            .build()
        canvas.save()
        canvas.translate(traitsTextStart, box3Top + 46f)
        traitsLayout.draw(canvas)
        canvas.restore()

        // Box 4: Character Overview (Row 3, Box A)
        val box4Left = 40f
        val box4Top = 1295f
        
        drawBoxHeader(
            canvas, context, R.drawable.ic_overview, "CHARACTER OVERVIEW",
            iconX = box4Left + 15f,
            iconY = box4Top + 15f,
            iconSize = iconSize,
            titleX = box4Left + 15f + iconSize + 10f,
            titleY = box4Top + 32f,
            titlePaint = titlePaint,
            goldColorValue = goldColorValue
        )
        val descTextStart = box4Left + 15f + iconSize + 10f
        val descLayout = StaticLayout.Builder.obtain(
            matchResult.character.description,
            0,
            matchResult.character.description.length,
            bodyPaintTaller,
            (459f - descTextStart - 15f).toInt()
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(4)
            .build()
        canvas.save()
        canvas.translate(descTextStart, box4Top + 46f)
        descLayout.draw(canvas)
        canvas.restore()

        // Box 5: Design Principles (Row 3, Box B)
        val box5Left = 475f
        val box5Top = 1295f
        
        drawBoxHeader(
            canvas, context, R.drawable.ic_principles, "DESIGN PRINCIPLES",
            iconX = box5Left + 15f,
            iconY = box5Top + 15f,
            iconSize = iconSize,
            titleX = box5Left + 15f + iconSize + 10f,
            titleY = box5Top + 32f,
            titlePaint = titlePaint,
            goldColorValue = goldColorValue
        )
        val principlesTextStart = box5Left + 15f + iconSize + 10f
        val principlesLayout = StaticLayout.Builder.obtain(
            matchResult.character.designBreakdown,
            0,
            matchResult.character.designBreakdown.length,
            bodyPaintTaller,
            (898f - principlesTextStart - 15f).toInt()
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(4)
            .build()
        canvas.save()
        canvas.translate(principlesTextStart, box5Top + 46f)
        principlesLayout.draw(canvas)
        canvas.restore()

        // Save Bitmap
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "share_card.png")
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { out ->
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Share Card")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private fun scaleAndCropCenter(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val sourceWidth = bitmap.width
        val sourceHeight = bitmap.height
        val scale = Math.max(newWidth.toFloat() / sourceWidth, newHeight.toFloat() / sourceHeight)
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2
        val target = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        matrix.postTranslate(left, top)
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        return target
    }

    private fun drawBoxHeader(
        canvas: Canvas,
        context: Context,
        iconResId: Int,
        title: String,
        iconX: Float,
        iconY: Float,
        iconSize: Float,
        titleX: Float,
        titleY: Float,
        titlePaint: Paint,
        goldColorValue: Int
    ) {
        val drawable = ContextCompat.getDrawable(context, iconResId)
        if (drawable != null) {
            drawable.setBounds(iconX.toInt(), iconY.toInt(), (iconX + iconSize).toInt(), (iconY + iconSize).toInt())
            drawable.setTint(goldColorValue)
            drawable.draw(canvas)
        }
        canvas.drawText(title, titleX, titleY, titlePaint)
    }
}
