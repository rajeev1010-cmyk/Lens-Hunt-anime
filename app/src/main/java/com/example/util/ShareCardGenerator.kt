package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.text.StaticLayout
import android.text.TextPaint
import android.text.Layout
import androidx.core.content.FileProvider
import com.example.domain.matcher.MatchResult
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ShareCardGenerator {
    suspend fun generateAndShare(
        context: Context,
        selfie: Bitmap,
        matchResult: MatchResult
    ) {
        val finalBitmap = withContext(Dispatchers.Default) {
            val width = 1080
            val height = 1350
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Background
            canvas.drawColor(Color.parseColor("#111111"))

            // Decorative borders
            val goldColor = Color.parseColor("#D4AF37")
            val goldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                style = Paint.Style.STROKE
                strokeWidth = 6f
            }
            val fillGoldPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                style = Paint.Style.FILL
            }

            // Outer border
            canvas.drawRect(40f, 40f, width - 40f, height - 40f, goldPaint)
            // Inner border
            canvas.drawRect(55f, 55f, width - 55f, height - 55f, goldPaint.apply { strokeWidth = 2f })

            // Corner accents
            canvas.drawRect(30f, 30f, 60f, 60f, fillGoldPaint)
            canvas.drawRect(width - 60f, 30f, width - 30f, 60f, fillGoldPaint)
            canvas.drawRect(30f, height - 60f, 60f, height - 30f, fillGoldPaint)
            canvas.drawRect(width - 60f, height - 60f, width - 30f, height - 30f, fillGoldPaint)

            // Logos
            val logoPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                textSize = 32f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                letterSpacing = 0.2f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("GIANT HUNT  ×  LENS HUNT", width / 2f, 120f, logoPaint)

            // Selfie Circular Frame
            val selfieRadius = 220f
            val selfieCx = width / 2f
            val selfieCy = 420f

            val scaledSelfie = scaleAndCropCenter(selfie, (selfieRadius * 2).toInt(), (selfieRadius * 2).toInt())

            val path = Path()
            path.addCircle(selfieCx, selfieCy, selfieRadius, Path.Direction.CW)
            canvas.save()
            canvas.clipPath(path)
            canvas.drawBitmap(scaledSelfie, selfieCx - selfieRadius, selfieCy - selfieRadius, null)
            canvas.restore()

            // Rings around selfie
            canvas.drawCircle(selfieCx, selfieCy, selfieRadius, goldPaint.apply { strokeWidth = 8f })
            canvas.drawCircle(selfieCx, selfieCy, selfieRadius + 15f, goldPaint.apply { strokeWidth = 2f })

            // Character Info
            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                textSize = 28f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                letterSpacing = 0.1f
                textAlign = Paint.Align.CENTER
            }
            val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = 64f
                typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
            }
            val seriesPaint = Paint(titlePaint).apply {
                color = Color.parseColor("#CCCCCC")
                textSize = 32f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                letterSpacing = 0f
            }

            var currentY = 740f
            canvas.drawText("ANIME DESIGN MATCH", width / 2f, currentY, titlePaint)
            currentY += 70f
            canvas.drawText(matchResult.character.name.uppercase(), width / 2f, currentY, namePaint)
            currentY += 50f
            canvas.drawText("${matchResult.character.series}  •  ${matchResult.similarityPercentage}% Match", width / 2f, currentY, seriesPaint)

            // Divider
            currentY += 50f
            canvas.drawLine(width / 2f - 150f, currentY, width / 2f + 150f, currentY, goldPaint.apply { strokeWidth = 2f })

            // Details Layout (Left & Right columns)
            val leftX = 120f
            val rightX = width / 2f + 40f
            val labelPaint = Paint(titlePaint).apply {
                textSize = 22f
                textAlign = Paint.Align.LEFT
            }
            val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = 24f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            }

            currentY += 60f
            
            // Designer
            canvas.drawText("DESIGNER", leftX, currentY, labelPaint)
            canvas.drawText(matchResult.character.designer, leftX, currentY + 35f, valuePaint)

            // Design Language
            canvas.drawText("DESIGN LANGUAGE", rightX, currentY, labelPaint)
            val langLayout = StaticLayout.Builder.obtain(matchResult.character.designLanguage, 0, matchResult.character.designLanguage.length, valuePaint, (width / 2f - 160f).toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setMaxLines(2)
                .build()
            canvas.save()
            canvas.translate(rightX, currentY + 10f)
            langLayout.draw(canvas)
            canvas.restore()

            currentY += 120f
            
            // Visual Traits
            canvas.drawText("VISUAL TRAITS", leftX, currentY, labelPaint)
            val traitsLayout = StaticLayout.Builder.obtain(matchResult.character.visualTraits, 0, matchResult.character.visualTraits.length, valuePaint, (width / 2f - 160f).toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setMaxLines(2)
                .build()
            canvas.save()
            canvas.translate(leftX, currentY + 10f)
            traitsLayout.draw(canvas)
            canvas.restore()

            // Design Principles
            canvas.drawText("PRINCIPLES", rightX, currentY, labelPaint)
            val principlesLayout = StaticLayout.Builder.obtain(matchResult.character.description, 0, matchResult.character.description.length, valuePaint, (width / 2f - 160f).toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setMaxLines(2)
                .build()
            canvas.save()
            canvas.translate(rightX, currentY + 10f)
            principlesLayout.draw(canvas)
            canvas.restore()

            // Footer / CTA
            val ctaY = height - 120f
            val ctaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                textSize = 32f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                letterSpacing = 0.05f
            }
            val urlPaint = Paint(ctaPaint).apply {
                color = Color.WHITE
                textSize = 24f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                letterSpacing = 0f
            }
            canvas.drawText("Find Your Anime Twin", width / 2f, ctaY, ctaPaint)
            canvas.drawText("www.gianthunt.com", width / 2f, ctaY + 40f, urlPaint)

            // Fake QR Code
            val qrSize = 120f
            val qrX = width / 2f
            val qrY = ctaY - 140f
            drawFakeQRCode(canvas, qrX, qrY, qrSize, goldColor)

            bitmap
        }

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

    private fun drawFakeQRCode(canvas: Canvas, cx: Float, cy: Float, size: Float, color: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { 
            this.color = Color.WHITE 
            style = Paint.Style.FILL
        }
        val half = size / 2
        canvas.drawRect(cx - half, cy - half, cx + half, cy + half, paint)
        
        paint.color = color
        canvas.drawRect(cx - half + 10f, cy - half + 10f, cx + half - 10f, cy + half - 10f, paint)
        
        paint.color = Color.WHITE
        canvas.drawRect(cx - half + 20f, cy - half + 20f, cx + half - 20f, cy + half - 20f, paint)
        
        paint.color = color
        canvas.drawRect(cx - 15f, cy - 15f, cx + 15f, cy + 15f, paint)
        
        // Extra blocks
        canvas.drawRect(cx - half + 10f, cy + 10f, cx - 10f, cy + half - 10f, paint)
        canvas.drawRect(cx + 10f, cy - half + 10f, cx + half - 10f, cy - 10f, paint)
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
