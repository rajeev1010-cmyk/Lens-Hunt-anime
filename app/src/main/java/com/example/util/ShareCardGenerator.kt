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
        val width = 1080
        val height = 1920

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
        val leftBoxX = 66f
        val boxY = 390f
        val photoWidth = 328f
        val photoHeight = 478f
        val photoRadius = 24f

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        val scaledSelfie = scaleAndCropCenter(selfie, photoWidth.toInt(), photoHeight.toInt())
        val photoPath = Path().apply { addRoundRect(RectF(leftBoxX, boxY, leftBoxX + photoWidth, boxY + photoHeight), photoRadius, photoRadius, Path.Direction.CW) }
        canvas.save()
        canvas.clipPath(photoPath)
        canvas.drawBitmap(scaledSelfie, leftBoxX, boxY, null)
        canvas.restore()
        canvas.drawRoundRect(RectF(leftBoxX, boxY, leftBoxX + photoWidth, boxY + photoHeight), photoRadius, photoRadius, borderPaint)

        // 3. YOUR ANIME TWIN (Right photo box) Label overlay
        // Let's draw a nice dark translucent pill overlay over the right box (silhouette is already there in bg image)
        val rightBoxX = 686f
        val nameOverlayRect = RectF(rightBoxX, boxY + photoHeight - 80f, rightBoxX + photoWidth, boxY + photoHeight)
        val overlayPath = Path().apply {
            addRoundRect(
                nameOverlayRect,
                floatArrayOf(0f, 0f, 0f, 0f, photoRadius, photoRadius, photoRadius, photoRadius),
                Path.Direction.CW
            )
        }
        val overlayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CC0B0B0B") // 80% opaque dark
            style = Paint.Style.FILL
        }
        canvas.save()
        canvas.clipPath(overlayPath)
        canvas.drawRect(nameOverlayRect, overlayPaint)
        canvas.restore()
        
        // Outline for overlay
        val overlayBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(nameOverlayRect, photoRadius, photoRadius, overlayBorderPaint)

        val charNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 28f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        canvas.drawText(matchResult.character.name, rightBoxX + photoWidth / 2f, boxY + photoHeight - 30f, charNamePaint)

        // 4. Center Rings - MATCH percentage info
        val centerCx = width / 2f
        val centerCy = boxY + photoHeight / 2f
        
        val matchTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            textSize = 24f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.05f
        }
        canvas.drawText("MATCH", centerCx, centerCy - 30f, matchTextPaint)
        canvas.drawText("${matchResult.similarityPercentage}%", centerCx, centerCy + 20f, matchTextPaint.apply { textSize = 48f; color = Color.WHITE })
        canvas.drawLine(centerCx - 60f, centerCy + 40f, centerCx + 60f, centerCy + 40f, dividerPaint)
        canvas.drawText("CONFIDENCE", centerCx, centerCy + 70f, matchTextPaint.apply { textSize = 16f; color = goldColorValue })

        // 5. Details Section (Dynamic Fields)
        val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 24f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            setShadowLayer(2f, 0f, 2f, Color.BLACK)
        }

        // Creator/Designer Value (Left)
        canvas.drawText(matchResult.character.designer, 110f, 995f, valuePaint)

        // Design Language Value (Right)
        canvas.drawText(matchResult.character.designLanguage, 590f, 995f, valuePaint)

        // Visual Traits Value (Span)
        val traitsLayout = StaticLayout.Builder.obtain(
            matchResult.character.visualTraits,
            0,
            matchResult.character.visualTraits.length,
            valuePaint,
            860
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(3)
            .build()
        canvas.save()
        canvas.translate(110f, 1130f)
        traitsLayout.draw(canvas)
        canvas.restore()

        // Character Description Value (Left)
        val descLayout = StaticLayout.Builder.obtain(
            matchResult.character.description,
            0,
            matchResult.character.description.length,
            valuePaint.apply { typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL); color = Color.parseColor("#CCCCCC") },
            380
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(12)
            .build()
        canvas.save()
        canvas.translate(110f, 1370f)
        descLayout.draw(canvas)
        canvas.restore()

        // Design Principles Value (Right)
        val principlesLayout = StaticLayout.Builder.obtain(
            matchResult.character.designPrinciples,
            0,
            matchResult.character.designPrinciples.length,
            valuePaint,
            380
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setMaxLines(12)
            .build()
        canvas.save()
        canvas.translate(590f, 1370f)
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
}
