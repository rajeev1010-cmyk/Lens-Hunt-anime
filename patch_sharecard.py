import re

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "r") as f:
    content = f.read()

content = content.replace(
    "suspend fun generateAndShare(context: Context, selfie: Bitmap, matchResult: MatchResult) {",
    "suspend fun generateAndShare(context: Context, selfie: Bitmap, matchResult: MatchResult, userFirstName: String, userSurname: String) {"
)

replacement = """
        val charNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            textSize = 64f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create("cursive", Typeface.BOLD)
            setShadowLayer(16f, 0f, 6f, Color.BLACK)
        }
        val matchNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CCCCCC")
            textSize = 32f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            setShadowLayer(8f, 0f, 4f, Color.BLACK)
        }
        
        // Draw the name in the center of the right box
        val rightBoxCenterY = boxY + (photoHeight / 2f)
        
        val nameLineHeight = charNamePaint.textSize * 1.1f
        val matchLineHeight = matchNamePaint.textSize * 1.5f
        val nameTotalHeight = nameLineHeight * 2 + matchLineHeight
        val nameStartY = rightBoxCenterY - nameTotalHeight / 2f - (charNamePaint.ascent() + charNamePaint.descent()) / 2f
        
        canvas.drawText(userFirstName, rightBoxX + rightPhotoWidth / 2f, nameStartY, charNamePaint)
        canvas.drawText(userSurname, rightBoxX + rightPhotoWidth / 2f, nameStartY + nameLineHeight, charNamePaint)
        canvas.drawText("(${matchResult.character.name})", rightBoxX + rightPhotoWidth / 2f, nameStartY + nameLineHeight + matchLineHeight, matchNamePaint)
"""

content = re.sub(
    r'val charNamePaint = Paint\(Paint.ANTI_ALIAS_FLAG\).*?for \(\(index, word\) in nameWords\.withIndex\(\)\) \{\s*canvas\.drawText\(word, rightBoxX \+ rightPhotoWidth / 2f, nameStartY \+ \(index \* nameLineHeight\), charNamePaint\)\s*\}',
    replacement.strip(),
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "w") as f:
    f.write(content)
