import re

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "r") as f:
    content = f.read()

content = content.replace(
    "suspend fun generateAndShare(context: Context, selfie: Bitmap, matchResult: MatchResult, userFirstName: String, userSurname: String) {",
    "suspend fun generateAndShare(context: Context, selfie: Bitmap, topMatches: List<MatchResult>, userFirstName: String, userSurname: String) {\n        val matchResult = topMatches.first()"
)

radar_logic = """        // 4. Center Area Map (Radar Chart)
        val centerCx = width / 2f
        val centerCyText = 722f
        
        val top5 = topMatches.take(5)
        val archetypeScores = mutableMapOf<String, Float>()
        for (match in top5) {
            val arch = match.character.archetype
            if (arch.isNotEmpty()) {
                archetypeScores[arch] = (archetypeScores[arch] ?: 0f) + match.similarityPercentage
            }
        }
        val sortedArchs = archetypeScores.entries.sortedByDescending { it.value }
        val top3Archetypes = mutableListOf<Pair<String, Float>>()
        for (i in 0 until 3) {
            if (i < sortedArchs.size) {
                top3Archetypes.add(Pair(sortedArchs[i].key, sortedArchs[i].value))
            } else {
                top3Archetypes.add(Pair("Unknown", 50f))
            }
        }

        // Normalize scores for the radar (max sum might be around 100-300, we scale relatively)
        val maxScore = top3Archetypes.maxOf { it.second }.coerceAtLeast(1f)
        
        val radarRadius = 110f
        
        // Draw radar background grids (e.g. 3 levels)
        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#44ECA72C")
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        val angles = listOf(-Math.PI / 2, Math.PI / 6, 5 * Math.PI / 6)
        
        for (level in 1..3) {
            val levelRadius = radarRadius * (level / 3f)
            val path = android.graphics.Path()
            for (i in 0 until 3) {
                val px = centerCx + (levelRadius * Math.cos(angles[i])).toFloat()
                val py = centerCyText + (levelRadius * Math.sin(angles[i])).toFloat()
                if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
            }
            path.close()
            canvas.drawPath(path, gridPaint)
        }
        
        // Draw area map
        val areaPath = android.graphics.Path()
        for (i in 0 until 3) {
            val scoreRatio = (top3Archetypes[i].second / maxScore).coerceIn(0.2f, 1f)
            val px = centerCx + (radarRadius * scoreRatio * Math.cos(angles[i])).toFloat()
            val py = centerCyText + (radarRadius * scoreRatio * Math.sin(angles[i])).toFloat()
            if (i == 0) areaPath.moveTo(px, py) else areaPath.lineTo(px, py)
        }
        areaPath.close()
        
        val areaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#88ECA72C")
            style = Paint.Style.FILL
        }
        val areaStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = goldColorValue
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawPath(areaPath, areaPaint)
        canvas.drawPath(areaPath, areaStrokePaint)
        
        // Draw labels on the rim
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 20f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            setShadowLayer(4f, 0f, 2f, Color.BLACK)
        }
        
        for (i in 0 until 3) {
            val labelRadius = radarRadius + 25f
            val px = centerCx + (labelRadius * Math.cos(angles[i])).toFloat()
            val py = centerCyText + (labelRadius * Math.sin(angles[i])).toFloat() + if (i == 0) -5f else 10f
            canvas.drawText(top3Archetypes[i].first, px, py, labelPaint)
        }"""

content = re.sub(
    r'// 4\. Center Rings - MATCH percentage info.*?// 5\. Details Section \(Dynamic Fields\)',
    radar_logic + '\n\n        // 5. Details Section (Dynamic Fields)',
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "w") as f:
    f.write(content)
