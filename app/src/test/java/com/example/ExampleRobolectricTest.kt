package com.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.gianthunt.lenshunt.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `find gold box coordinates`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val options = BitmapFactory.Options().apply {
        inScaled = false
    }
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.animetwin_sharecard, options)
    if (bitmap == null) {
        println("--- DIAGNOSTIC: Could not load animetwin_sharecard ---")
        return
    }
    println("--- DIAGNOSTIC: Loaded bitmap ${bitmap.width}x${bitmap.height} ---")
    
    // We want to find the non-black segments at X = 150
    // This will correspond to borders, frames, and background highlights.
    val scanX = 150
    var nonBlackSegments = mutableListOf<Int>()
    for (y in 0 until bitmap.height) {
        val pixel = bitmap.getPixel(scanX, y)
        val r = Color.red(pixel)
        val g = Color.green(pixel)
        val b = Color.blue(pixel)
        
        // Let's find pixels that are noticeably brighter than the black background
        if (r + g + b > 60) {
            nonBlackSegments.add(y)
        }
    }
    
    println("--- DIAGNOSTIC: Non-black pixels at X=$scanX: ---")
    var lastY = -1
    var groupStart = -1
    for (y in nonBlackSegments) {
        if (groupStart == -1) {
            groupStart = y
        } else if (y - lastY > 20) {
            // Convert to the target height of 1920 (bitmap height might be 1672 or similar)
            val targetStart = (groupStart.toFloat() / bitmap.height * 1920).toInt()
            val targetEnd = (lastY.toFloat() / bitmap.height * 1920).toInt()
            println("Non-black block (1920 scaled): Y = $targetStart to $targetEnd (orig $groupStart to $lastY)")
            groupStart = y
        }
        lastY = y
    }
    if (groupStart != -1) {
        val targetStart = (groupStart.toFloat() / bitmap.height * 1920).toInt()
        val targetEnd = (lastY.toFloat() / bitmap.height * 1920).toInt()
        println("Non-black block (1920 scaled): Y = $targetStart to $targetEnd (orig $groupStart to $lastY)")
    }
  }
}



