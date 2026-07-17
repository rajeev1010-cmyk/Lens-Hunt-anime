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
        val headerGoldPaint = Paint(headerPaint).apply { color = goldColor }
        
        // Left: Giant Hunt Logo
        val giantDrawable = ContextCompat.getDrawable(context, R.drawable.logo_giant_hunt)
        giantDrawable?.setBounds(80, 80, 80 + 360, 80 + 120)
        giantDrawable?.draw(canvas)
        
        canvas.drawText("OFFICIAL", width - 320f, 130f, Paint(headerPaint).apply { textSize = 32f })
        canvas.drawText("ATTEMPT", width - 320f, 170f, Paint(headerPaint).apply { textSize = 32f })
        
        // Divider line between GWR and text
        canvas.drawLine(width / 2f + 120f, 100f, width / 2f + 120f, 180f, innerBorderPaint)
        canvas.drawLine(width / 2f - 120f, 100f, width / 2f - 120f, 180f, innerBorderPaint)
        
        // Center: GWR Logo
        val gwrDrawable = ContextCompat.getDrawable(context, R.drawable.logo_gwr)
        gwrDrawable?.setBounds((width / 2f - 80f).toInt(), 70, (width / 2f + 80f).toInt(), 210)
        gwrDrawable?.draw(canvas)

        // ANIME TWIN MATCH Divider
        var currentY = 280f
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

        // Photo boxes
        currentY += 80f
        val photoWidth = 340f
        val photoHeight = 480f
        val photoRadius = 24f
        val leftBoxX = 70f
        val rightBoxX = width - photoWidth - 70f

        val boxLabelPaint = Paint(sectionTitlePaint).apply { textSize = 22f; letterSpacing = 0.05f }
        canvas.drawText("YOUR SELFIE", leftBoxX + photoWidth / 2, currentY, boxLabelPaint)
        canvas.drawText("YOUR ANIME TWIN", rightBoxX + photoWidth / 2, currentY, boxLabelPaint)
        
        val boxY = currentY + 30f
        val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColor; style = Paint.Style.STROKE; strokeWidth = 3f }
        val fillBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#151515"); style = Paint.Style.FILL }
        
        val leftRect = RectF(leftBoxX, boxY, leftBoxX + photoWidth, boxY + photoHeight)
        val rightRect = RectF(rightBoxX, boxY, rightBoxX + photoWidth, boxY + photoHeight)
        
        val scaledSelfie = scaleAndCropCenter(selfie, photoWidth.toInt(), photoHeight.toInt())
        val photoPath = Path().apply { addRoundRect(leftRect, photoRadius, photoRadius, Path.Direction.CW) }
        canvas.save()
        canvas.clipPath(photoPath)
        canvas.drawBitmap(scaledSelfie, leftBoxX, boxY, null)
        canvas.restore()
        canvas.drawRoundRect(leftRect, photoRadius, photoRadius, boxPaint)

        canvas.drawRoundRect(rightRect, photoRadius, photoRadius, fillBoxPaint)
        canvas.drawRoundRect(rightRect, photoRadius, photoRadius, boxPaint)
        val twinNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 36f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        canvas.drawText(matchResult.character.name, rightBoxX + photoWidth / 2, boxY + photoHeight / 2, twinNamePaint)

        // Center Rings
        val centerCx = width / 2f
        val centerCy = boxY + photoHeight / 2
        val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColor; style = Paint.Style.STROKE; strokeWidth = 1f }
        
        // Draw the concentric circles like the template
        canvas.drawCircle(centerCx, centerCy, 160f, ringPaint.apply { strokeWidth = 1f; pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f) })
        canvas.drawCircle(centerCx, centerCy, 140f, ringPaint.apply { strokeWidth = 2f; pathEffect = null })
        canvas.drawCircle(centerCx, centerCy, 120f, ringPaint.apply { strokeWidth = 4f })
        
        // Add tiny diamonds on the middle ring
        val ringDiamondPath = Path().apply {
            moveTo(0f, -8f)
            lineTo(8f, 0f)
            lineTo(0f, 8f)
            lineTo(-8f, 0f)
            close()
        }
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColor; style = Paint.Style.FILL }
        canvas.save()
        canvas.translate(centerCx, centerCy - 140f)
        canvas.drawPath(ringDiamondPath, p)
        canvas.restore()
        canvas.save()
        canvas.translate(centerCx, centerCy + 140f)
        canvas.drawPath(ringDiamondPath, p)
        canvas.restore()
        canvas.save()
        canvas.translate(centerCx - 140f, centerCy)
        canvas.drawPath(ringDiamondPath, p)
        canvas.restore()
        canvas.save()
        canvas.translate(centerCx + 140f, centerCy)
        canvas.drawPath(ringDiamondPath, p)
        canvas.restore()

        val matchTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColor
            textSize = 24f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.05f
        }
        canvas.drawText("MATCH", centerCx, centerCy - 30f, matchTextPaint)
        canvas.drawText("${matchResult.similarityPercentage}%", centerCx, centerCy + 20f, matchTextPaint.apply { textSize = 48f; color = Color.WHITE })
        canvas.drawLine(centerCx - 60f, centerCy + 40f, centerCx + 60f, centerCy + 40f, dividerPaint)
        canvas.drawText("CONFIDENCE", centerCx, centerCy + 70f, matchTextPaint.apply { textSize = 16f; color = goldColor })

        // Mid Divider below photos
        currentY = boxY + photoHeight + 80f
        canvas.drawLine(80f, currentY, width / 2f - 20f, currentY, dividerPaint)
        canvas.drawLine(width / 2f + 20f, currentY, width - 80f, currentY, dividerPaint)
        // Diamond
        val diamondPath = Path().apply {
            moveTo(width / 2f, currentY - 12f)
            lineTo(width / 2f + 12f, currentY)
            lineTo(width / 2f, currentY + 12f)
            lineTo(width / 2f - 12f, currentY)
            close()
        }
        canvas.drawPath(diamondPath, cornerPaint)

        // Details Section
        currentY += 100f
        val leftCol = 160f
        val rightCol = width / 2f + 120f
        
        val labelPaint = Paint(boxLabelPaint).apply { textAlign = Paint.Align.LEFT; textSize = 22f }
        val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CCCCCC") // off white
            textSize = 24f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            setShadowLayer(2f, 0f, 2f, Color.BLACK)
        }
        
        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = goldColor; style = Paint.Style.STROKE; strokeWidth = 3f }

        fun drawField(label: String, value: String, x: Float, y: Float) {
            canvas.drawText(label, x, y, labelPaint)
            // draw small circle as icon placeholder
            canvas.drawCircle(x - 40f, y - 8f, 15f, iconPaint)
            
            val layout = StaticLayout.Builder.obtain(value, 0, value.length, valuePaint, (width / 2f - 200f).toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setMaxLines(3)
                .build()
            canvas.save()
            canvas.translate(x, y + 20f)
            layout.draw(canvas)
            canvas.restore()
        }

        // Row 1
        drawField("CREATOR / DESIGNER", matchResult.character.designer, leftCol, currentY)
        drawField("DESIGN LANGUAGE", matchResult.character.designLanguage, rightCol, currentY)
        
        // Row 2
        currentY += 160f
        drawField("VISUAL TRAITS", matchResult.character.visualTraits, leftCol, currentY)
        drawField("DESIGN PRINCIPLES", matchResult.character.designPrinciples, rightCol, currentY)
        
        // Row 3
        currentY += 160f
        drawField("CHARACTER OVERVIEW", matchResult.character.description, leftCol, currentY)

        // Key Colors
        currentY += 180f
        canvas.drawText("KEY COLORS", leftCol, currentY, labelPaint)
        canvas.drawCircle(leftCol - 40f, currentY - 8f, 15f, iconPaint) // palette icon placeholder
        val colorRadius = 30f
        var colorX = leftCol + 180f
        for (i in 0..4) {
            canvas.drawCircle(colorX, currentY + 60f, colorRadius, iconPaint)
            colorX += 90f
        }

        // Character Trait
        currentY += 180f
        canvas.drawText("CHARACTER TRAIT", leftCol, currentY, labelPaint)
        canvas.drawCircle(leftCol - 40f, currentY - 8f, 15f, iconPaint) // shield icon placeholder
        
        val iconY = currentY + 70f
        var iconX = leftCol + 60f
        
        canvas.drawRect(iconX - 20f, iconY - 25f, iconX + 20f, iconY + 25f, iconPaint) 
        canvas.drawLine(iconX + 60f, iconY - 30f, iconX + 60f, iconY + 30f, dividerPaint)
        iconX += 120f
        canvas.drawCircle(iconX, iconY, 25f, iconPaint) 
        canvas.drawLine(iconX + 60f, iconY - 30f, iconX + 60f, iconY + 30f, dividerPaint)
        iconX += 120f
        canvas.drawCircle(iconX, iconY, 25f, iconPaint) 
        canvas.drawLine(iconX + 60f, iconY - 30f, iconX + 60f, iconY + 30f, dividerPaint)
        iconX += 120f
        canvas.drawRect(iconX - 10f, iconY - 20f, iconX + 10f, iconY + 20f, iconPaint) 
        canvas.drawLine(iconX + 60f, iconY - 30f, iconX + 60f, iconY + 30f, dividerPaint)
        iconX += 120f
        canvas.drawCircle(iconX, iconY, 25f, iconPaint)

        // Footer
        currentY = height - 100f
        val footerPaint = Paint(labelPaint).apply { textSize = 20f }
        canvas.drawText("REAL WORLD IDENTITY", leftCol, currentY, footerPaint)
        canvas.drawCircle(leftCol - 30f, currentY - 6f, 12f, iconPaint)
        
        canvas.drawLine(width / 2f, currentY - 30f, width / 2f, currentY + 10f, dividerPaint)
        
        canvas.drawText("GIANTVERSE.COM", rightCol + 40f, currentY, footerPaint)
        canvas.drawCircle(rightCol + 10f, currentY - 6f, 12f, iconPaint)
        
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
