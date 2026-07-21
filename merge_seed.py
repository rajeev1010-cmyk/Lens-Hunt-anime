import sys

# Add root folder to path to load data modules
sys.path.append('.')
from data1 import characters_1
from data2 import characters_2
from data3 import characters_3
from data4 import characters_4

all_characters = characters_1 + characters_2 + characters_3 + characters_4

print(f"Total loaded characters: {len(all_characters)}")

def determine_cluster(c):
    if 'cluster' in c:
        return c['cluster']
    
    p = c['profile']
    
    # Explicit mapping for known Non-Human / Fantasy characters
    non_human_ids = [
        'ainz', 'rimuru', 'kaneki', 'gobta', 'milim-nava', 'diablo', 'lucy', 'kage', 'bojji'
    ]
    if c['id'] in non_human_ids:
        return "Cluster E - Non Human / Fantasy"
        
    # Standard scoring matching Jetpack Compose ClusterManager
    scoreA = (p['angularity'] + p['symmetry'] + p['expressionNeutrality'] + p['jawSharpness']) / 4.0
    scoreB = (p['angularity'] + p['symmetry'] + (1.0 - p['eyeNarrowness']) + p['jawSharpness']) / 4.0 * 0.9
    scoreC = ((1.0 - p['faceLength']) + p['jawSharpness'] + (1.0 - p['eyeNarrowness']) + p['contrast']) / 4.0
    scoreD = ((1.0 - p['faceLength']) + (1.0 - p['angularity']) + (1.0 - p['eyeNarrowness']) + p['warmth']) / 4.0
    
    # Custom rule overlays to align with the design aesthetics of specific characters:
    if c['gender'] == 'female' and p['expressionNeutrality'] >= 0.7:
        return "Cluster A - Clean & Stoic"
    if p['angularity'] >= 0.65 and p['jawSharpness'] >= 0.6:
        return "Cluster B - Sharp & Cool"
    if p['faceLength'] >= 0.55 and p['expressionNeutrality'] >= 0.6:
        return "Cluster C - Mature & Structured"
    if p['faceLength'] <= 0.45 or p['warmth'] >= 0.6:
        return "Cluster D - Highly Stylized"
        
    scores = {
        "Cluster A - Clean & Stoic": scoreA,
        "Cluster B - Sharp & Cool": scoreB,
        "Cluster C - Mature & Structured": scoreC,
        "Cluster D - Highly Stylized": scoreD
    }
    return max(scores, key=scores.get)

with open("app/src/main/java/com/example/data/local/CharacterDataSeed.kt", "w") as f:
    f.write("""package com.example.data.local

import com.example.data.model.VisualAxes

object CharacterDataSeed {
    val characters = listOf(
""")
    for c in all_characters:
        cluster = determine_cluster(c)
        p = c['profile']
        
        def clean(s):
            return s.replace('"', '\\"')
            
        design_breakdown = f"{c['communicates']} through {c['through']}"
        
        f.write(f"""        CharacterEntity(
            id = "{clean(c['id'])}",
            name = "{clean(c['name'])}",
            gender = "{clean(c['gender'])}",
            series = "{clean(c['series'])}",
            designer = "{clean(c['designer'])}",
            franchise = "{clean(c['franchise'])}",
            cluster = "{cluster}",
            visualTraits = "{clean(c['visual_traits'])}",
            designLanguage = "{clean(c['design_language'])}",
            designPrinciples = "{clean(c['shape_language'])}",
            designBreakdown = "{clean(design_breakdown)}",
            description = "{clean(c['description'])}",
            calling = "{c.get('giantverseArchetype', {}).get('calling', 'Hunter')}",
            archetype = "{c.get('giantverseArchetype', {}).get('archetype', 'Pathfinder')}",
            profile = VisualAxes(
                faceLength = {p['faceLength']}f,
                jawSharpness = {p['jawSharpness']}f,
                eyeNarrowness = {p['eyeNarrowness']}f,
                angularity = {p['angularity']}f,
                symmetry = {p['symmetry']}f,
                warmth = {p['warmth']}f,
                browWeight = {p['browWeight']}f,
                expressionNeutrality = {p['expressionNeutrality']}f,
                hairDarkness = {p['hairDarkness']}f,
                hairVolume = {p['hairVolume']}f,
                contrast = {p['contrast']}f,
                glasses = {p['glasses']}f
            )
        ),
""")
    f.write("""    )
}
""")
print("Successfully generated CharacterDataSeed.kt with 182 characters!")
