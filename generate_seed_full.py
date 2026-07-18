import sys
from data_new_all import characters

template = """package com.example.data.local

import com.example.data.model.VisualAxes

object CharacterDataSeed {
    val characters = listOf(
%s
    )
}
"""

char_template = """        CharacterEntity(
            id = "{id}",
            name = "{name}",
            gender = "{gender}",
            series = "{series}",
            creator = "{creator}",
            designer = "{designer}",
            studio = "{studio}",
            franchise = "{franchise}",
            aliases = "{aliases}",
            publisher = "{publisher}",
            firstAppearance = "{firstAppearance}",
            collection = "{collection}",
            visualTraits = "{visualTraits}",
            designLanguage = "{designLanguage}",
            designPrinciples = "{designPrinciples}",
            primaryColors = "{primaryColors}",
            silhouette = "{silhouette}",
            archetype = "{archetype}",
            temperament = "{temperament}",
            keywords = "{keywords}",
            copyrightNotice = "",
            description = "{description}",
            cluster = "{cluster}",
            designBreakdown = "{designBreakdown}",
            profile = VisualAxes(
                faceShape = {faceShape}f,
                jawWidthRatio = {jawWidthRatio}f,
                jawAngle = {jawAngle}f,
                chinLengthRatio = {chinLengthRatio}f,
                chinSharpness = {chinSharpness}f,
                foreheadWidthRatio = {foreheadWidthRatio}f,
                foreheadHeightRatio = {foreheadHeightRatio}f,
                cheekboneWidthRatio = {cheekboneWidthRatio}f,
                faceHeightRatio = {faceHeightRatio}f,
                eyeSizeRatio = {eyeSizeRatio}f,
                eyeSpacingRatio = {eyeSpacingRatio}f,
                eyeTilt = {eyeTilt}f,
                eyeRoundness = {eyeRoundness}f,
                eyebrowThickness = {eyebrowThickness}f,
                eyebrowCurve = {eyebrowCurve}f,
                noseLengthRatio = {noseLengthRatio}f,
                noseWidthRatio = {noseWidthRatio}f,
                mouthWidthRatio = {mouthWidthRatio}f,
                lipThickness = {lipThickness}f,
                earSizeRatio = {earSizeRatio}f,
                hairlineHeight = {hairlineHeight}f,
                neckWidthRatio = {neckWidthRatio}f,
                symmetry = {symmetry}f,
                expressionNeutrality = {expressionNeutrality}f,
                stylizationIndex = {stylizationIndex}f
            )
        )"""

def determine_cluster(stylizationIndex, gender):
    if stylizationIndex >= 0.8:
        return "Cluster D - Expressive & Bold"
    elif stylizationIndex <= 0.3:
        return "Cluster B - Sharp & Cool"
    elif gender == "female":
        return "Cluster C - Soft & Cute"
    elif stylizationIndex >= 0.5:
        return "Cluster A - Clean & Stoic"
    else:
        return "Cluster E - Unique & Ethereal"

char_strings = []
for c in characters:
    fm = c['facialMetrics']
    prof = c.get('profile', {})
    
    # We add symmetry and expressionNeutrality from profile, stylizationIndex from fm
    symmetry = prof.get('symmetry', 0.5)
    expressionNeutrality = prof.get('expressionNeutrality', 0.5)
    
    cluster = determine_cluster(fm.get('stylizationIndex', 0.5), c['gender'])
    
    char_str = char_template.format(
        id=c['id'],
        name=c['name'].replace('"', '\\"'),
        gender=c['gender'],
        series=c['series'].replace('"', '\\"'),
        creator=c['creator'].replace('"', '\\"'),
        designer=(c.get('characterDesigner') or "Unknown").replace('"', '\\"'),
        studio=c['studio'].replace('"', '\\"'),
        franchise=c['franchise'].replace('"', '\\"'),
        aliases=", ".join(c.get('aliases', [])).replace('"', '\\"'),
        publisher=c['publisher'].replace('"', '\\"'),
        firstAppearance=c['firstAppearance'].replace('"', '\\"'),
        collection=c['collection'],
        visualTraits=", ".join(c.get('visual_traits', [])).replace('"', '\\"'),
        designLanguage=", ".join(c.get('design_language', [])).replace('"', '\\"'),
        designPrinciples=c.get('shape_language', "Unknown").replace('"', '\\"'),
        primaryColors=", ".join(c.get('primary_colors', [])).replace('"', '\\"'),
        silhouette=c.get('silhouette', "Unknown").replace('"', '\\"'),
        archetype=c.get('archetype', "Unknown").replace('"', '\\"'),
        temperament=c.get('temperament', "Unknown").replace('"', '\\"'),
        keywords=", ".join(c.get('keywords', [])).replace('"', '\\"'),
        description=c.get('description', "Desc").replace('"', '\\"'),
        cluster=cluster,
        designBreakdown="Communicates: " + ", ".join(c.get('design_breakdown', {}).get('communicates', [])) + " Through: " + ", ".join(c.get('design_breakdown', {}).get('through', [])),
        faceShape=fm.get('faceShape', 0.5),
        jawWidthRatio=fm.get('jawWidthRatio', 0.5),
        jawAngle=fm.get('jawAngle', 0.5),
        chinLengthRatio=fm.get('chinLengthRatio', 0.5),
        chinSharpness=fm.get('chinSharpness', 0.5),
        foreheadWidthRatio=fm.get('foreheadWidthRatio', 0.5),
        foreheadHeightRatio=fm.get('foreheadHeightRatio', 0.5),
        cheekboneWidthRatio=fm.get('cheekboneWidthRatio', 0.5),
        faceHeightRatio=fm.get('faceHeightRatio', 0.5),
        eyeSizeRatio=fm.get('eyeSizeRatio', 0.5),
        eyeSpacingRatio=fm.get('eyeSpacingRatio', 0.5),
        eyeTilt=fm.get('eyeTilt', 0.5),
        eyeRoundness=fm.get('eyeRoundness', 0.5),
        eyebrowThickness=fm.get('eyebrowThickness', 0.5),
        eyebrowCurve=fm.get('eyebrowCurve', 0.5),
        noseLengthRatio=fm.get('noseLengthRatio', 0.5),
        noseWidthRatio=fm.get('noseWidthRatio', 0.5),
        mouthWidthRatio=fm.get('mouthWidthRatio', 0.5),
        lipThickness=fm.get('lipThickness', 0.5),
        earSizeRatio=fm.get('earSizeRatio', 0.5),
        hairlineHeight=fm.get('hairlineHeight', 0.5),
        neckWidthRatio=fm.get('neckWidthRatio', 0.5),
        symmetry=symmetry,
        expressionNeutrality=expressionNeutrality,
        stylizationIndex=fm.get('stylizationIndex', 0.5)
    )
    char_strings.append(char_str)

final_kt = template % (",\n".join(char_strings))

with open("app/src/main/java/com/example/data/local/CharacterDataSeed.kt", "w") as f:
    f.write(final_kt)

print("Successfully generated CharacterDataSeed.kt with", len(characters), "characters!")
