package com.example.data.local

import com.example.data.model.VisualAxes

object CharacterDataSeed {
    val characters = listOf(
        CharacterEntity(
            id = "levi", name = "Levi", series = "Attack on Titan", designer = "Kyoji Asano", franchise = "AoT",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Narrow eyes, undercut, stern brow, sharp jawline", designLanguage = "Stoic Guardian",
            designPrinciples = "Minimalism, Sharpness, Utility over expression", designBreakdown = "Narrow eye shapes communicate intense focus, while the sharp jaw provides a sense of mature composure.", description = "A hyper-competent combatant characterized by a serious, immovable expression.",
            profile = VisualAxes(
                faceLength = 0.38f, jawSharpness = 0.65f, eyeNarrowness = 0.7f,
                angularity = 0.75f, symmetry = 0.85f, warmth = 0.2f,
                browWeight = 0.7f, expressionNeutrality = 0.9f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "mikasa", name = "Mikasa", series = "Attack on Titan", designer = "Kyoji Asano", franchise = "AoT",
            cluster = "Cluster A - Clean & Stoic", visualTraits = "Dark hair, calm gaze, subtle angularity", designLanguage = "Reserved Protector",
            designPrinciples = "Quiet strength, subtle melancholy", designBreakdown = "Slightly softer jawline than Levi, but with equally intense and narrow eyes that communicate resolve.", description = "Fiercely loyal and highly capable, her design emphasizes quiet sorrow and strength.",
            profile = VisualAxes(
                faceLength = 0.4f, jawSharpness = 0.55f, eyeNarrowness = 0.5f,
                angularity = 0.6f, symmetry = 0.8f, warmth = 0.3f,
                browWeight = 0.6f, expressionNeutrality = 0.8f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "violet", name = "Violet", series = "Violet Evergarden", designer = "Akiko Takase", franchise = "Kyoto Animation",
            cluster = "Cluster A - Clean & Stoic", visualTraits = "Large expressive eyes (later), mechanical precision initially, soft color palette", designLanguage = "Elegant Automaton",
            designPrinciples = "Beauty in mechanical precision turning into emotional warmth", designBreakdown = "Perfect symmetry and classical beauty ratios create an almost doll-like initial impression.", description = "An ex-soldier learning to understand human emotions, her design reflects a beautiful but tragic doll.",
            profile = VisualAxes(
                faceLength = 0.42f, jawSharpness = 0.45f, eyeNarrowness = 0.3f,
                angularity = 0.4f, symmetry = 0.95f, warmth = 0.4f,
                browWeight = 0.4f, expressionNeutrality = 0.75f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "loid", name = "Loid", series = "Spy x Family", designer = "Kazuaki Shimada", franchise = "Spy x Family",
            cluster = "Cluster C - Mature & Structured", visualTraits = "Sharp features, blonde hair, composed expression", designLanguage = "Suave Professional",
            designPrinciples = "Deceptive perfection, classical masculine elegance", designBreakdown = "Strong, straight nose and defined jawline create a classically handsome and trustworthy appearance.", description = "A master spy whose appearance is carefully constructed to be impeccably perfect and unreadable.",
            profile = VisualAxes(
                faceLength = 0.45f, jawSharpness = 0.6f, eyeNarrowness = 0.45f,
                angularity = 0.65f, symmetry = 0.9f, warmth = 0.5f,
                browWeight = 0.65f, expressionNeutrality = 0.8f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "yor", name = "Yor", series = "Spy x Family", designer = "Kazuaki Shimada", franchise = "Spy x Family",
            cluster = "Cluster A - Clean & Stoic", visualTraits = "Soft face but sharp eyes in combat, black hair, gentle default smile", designLanguage = "Hidden Assassin",
            designPrinciples = "Contrast between soft domesticity and sharp lethality", designBreakdown = "Soft, rounded facial contours contrast with incredibly sharp, focused eye designs when serious.", description = "A deadly assassin posing as a mild-mannered clerk, featuring dual design modes.",
            profile = VisualAxes(
                faceLength = 0.38f, jawSharpness = 0.4f, eyeNarrowness = 0.35f,
                angularity = 0.45f, symmetry = 0.85f, warmth = 0.7f,
                browWeight = 0.45f, expressionNeutrality = 0.5f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "spike", name = "Spike", series = "Cowboy Bebop", designer = "Toshihiro Kawamoto", franchise = "Cowboy Bebop",
            cluster = "Cluster C - Mature & Structured", visualTraits = "Tall, lanky, relaxed features, messy hair", designLanguage = "Languid Cool",
            designPrinciples = "Effortless style, underlying tragedy", designBreakdown = "Long face and slightly drooping eyes create a permanently relaxed, unbothered expression.", description = "A space cowboy with a dark past, hiding his skills behind a slacker facade.",
            profile = VisualAxes(
                faceLength = 0.5f, jawSharpness = 0.55f, eyeNarrowness = 0.55f,
                angularity = 0.6f, symmetry = 0.7f, warmth = 0.4f,
                browWeight = 0.55f, expressionNeutrality = 0.75f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "rukia", name = "Rukia", series = "Bleach", designer = "Tite Kubo", franchise = "Bleach",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Large sharp eyes, angular jaw, petite frame", designLanguage = "Fierce Elegance",
            designPrinciples = "Sharpness, nobility, intense gaze", designBreakdown = "Highly angular eyes paired with a sharp, small chin emphasize her intense and serious nature.", description = "A noble Soul Reaper whose strict demeanor is matched by her sharp visual design.",
            profile = VisualAxes(
                faceLength = 0.35f, jawSharpness = 0.6f, eyeNarrowness = 0.25f,
                angularity = 0.7f, symmetry = 0.8f, warmth = 0.3f,
                browWeight = 0.6f, expressionNeutrality = 0.65f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "eren", name = "Eren", series = "Attack on Titan", designer = "Kyoji Asano", franchise = "AoT",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Fierce glare, strong brows, messy hair", designLanguage = "Aggressive Drive",
            designPrinciples = "Intensity, anger, forward momentum", designBreakdown = "Heavy, lowered brows and intensely focused eyes convey unrelenting determination and anger.", description = "Driven by a singular, violent goal, his facial features reflect raw emotional intensity.",
            profile = VisualAxes(
                faceLength = 0.42f, jawSharpness = 0.6f, eyeNarrowness = 0.4f,
                angularity = 0.65f, symmetry = 0.75f, warmth = 0.2f,
                browWeight = 0.85f, expressionNeutrality = 0.4f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "gojo", name = "Gojo", series = "Jujutsu Kaisen", designer = "Tadashi Hiramatsu", franchise = "JJK",
            cluster = "Cluster C - Mature & Structured", visualTraits = "Blindfold, white hair, very tall, sharp jaw", designLanguage = "Untouchable Arrogance",
            designPrinciples = "Divine confidence, obscured vision", designBreakdown = "The lower half of his face is sharply defined, contrasting with his covered eyes to create mystique.", description = "The strongest sorcerer, incredibly arrogant but deeply caring.",
            profile = VisualAxes(
                faceLength = 0.48f, jawSharpness = 0.7f, eyeNarrowness = 0.5f,
                angularity = 0.65f, symmetry = 0.95f, warmth = 0.5f,
                browWeight = 0.5f, expressionNeutrality = 0.6f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "sasuke", name = "Sasuke", series = "Naruto", designer = "Tetsuya Nishio", franchise = "Naruto",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Dark features, sharp eyes, spiky back hair", designLanguage = "Avenger's Edge",
            designPrinciples = "Coolness, tragedy, sharpness", designBreakdown = "Very sharp, angled eyes and a prominent brow emphasize his continuous state of anger and focus.", description = "A prodigy driven by revenge, with a design that screams 'cool and unapproachable'.",
            profile = VisualAxes(
                faceLength = 0.4f, jawSharpness = 0.65f, eyeNarrowness = 0.45f,
                angularity = 0.7f, symmetry = 0.85f, warmth = 0.2f,
                browWeight = 0.75f, expressionNeutrality = 0.8f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "sung-jinwoo", name = "Sung Jinwoo", series = "Solo Leveling", designer = "Tomoko Sudo", franchise = "Solo Leveling",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Glowing eyes, sharp jaw, sleek hair", designLanguage = "Shadow Monarch",
            designPrinciples = "Transformation, overwhelming power, edge", designBreakdown = "Extremely sharp V-shaped jaw and narrow, glowing eyes give him an imposing, predator-like aesthetic.", description = "A weak hunter who transformed into an unstoppable, edgy powerhouse.",
            profile = VisualAxes(
                faceLength = 0.44f, jawSharpness = 0.75f, eyeNarrowness = 0.6f,
                angularity = 0.8f, symmetry = 0.9f, warmth = 0.1f,
                browWeight = 0.8f, expressionNeutrality = 0.85f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "cloud", name = "Cloud", series = "Final Fantasy VII", designer = "Tetsuya Nomura", franchise = "Final Fantasy",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Spiky blonde hair, glowing eyes, sharp chin", designLanguage = "Fractured Soldier",
            designPrinciples = "Iconic silhouette, cold exterior", designBreakdown = "Sharp angular features combined with large, intense eyes reflect his strong but broken persona.", description = "A mercenary with a complex past and an iconic, sharp-edged design.",
            profile = VisualAxes(
                faceLength = 0.42f, jawSharpness = 0.6f, eyeNarrowness = 0.4f,
                angularity = 0.65f, symmetry = 0.85f, warmth = 0.3f,
                browWeight = 0.65f, expressionNeutrality = 0.75f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "2b", name = "2B", series = "NieR:Automata", designer = "Akihiko Yoshida", franchise = "NieR",
            cluster = "Cluster A - Clean & Stoic", visualTraits = "Blindfold, silver bob, pale skin", designLanguage = "Gothic Android",
            designPrinciples = "Elegance, lack of vision, tragic beauty", designBreakdown = "A delicate, rounded chin contrasts heavily with her combat-ready attire and obscured eyes.", description = "A combat android whose beautiful, delicate lower face belies her lethal capabilities.",
            profile = VisualAxes(
                faceLength = 0.38f, jawSharpness = 0.45f, eyeNarrowness = 0.5f,
                angularity = 0.4f, symmetry = 0.95f, warmth = 0.2f,
                browWeight = 0.5f, expressionNeutrality = 0.9f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "makima", name = "Makima", series = "Chainsaw Man", designer = "Kiyotaka Oshiyama", franchise = "Chainsaw Man",
            cluster = "Cluster C - Mature & Structured", visualTraits = "Ringed eyes, soft smile, business attire", designLanguage = "Predatory Calm",
            designPrinciples = "Unnerving stillness, false warmth", designBreakdown = "Perfectly symmetrical, calm features paired with hypnotic ringed eyes create an intensely unnerving aura.", description = "A high-ranking devil hunter whose serene beauty masks total control and ruthlessness.",
            profile = VisualAxes(
                faceLength = 0.4f, jawSharpness = 0.5f, eyeNarrowness = 0.35f,
                angularity = 0.45f, symmetry = 0.98f, warmth = 0.6f,
                browWeight = 0.4f, expressionNeutrality = 0.85f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "ichigo", name = "Ichigo", series = "Bleach", designer = "Tite Kubo", franchise = "Bleach",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Orange hair, permanent scowl, sharp eyes", designLanguage = "Defiant Punk",
            designPrinciples = "Rebellion, sharpness, protective instinct", designBreakdown = "A constant frown and sharp, angular eyes give him an aggressive, unapproachable resting face.", description = "A hot-headed teenager with a strong sense of duty and a sharp, iconic design.",
            profile = VisualAxes(
                faceLength = 0.45f, jawSharpness = 0.65f, eyeNarrowness = 0.45f,
                angularity = 0.7f, symmetry = 0.75f, warmth = 0.4f,
                browWeight = 0.8f, expressionNeutrality = 0.3f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "naruto", name = "Naruto", series = "Naruto", designer = "Tetsuya Nishio", franchise = "Naruto",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Whisker marks, bright blonde hair, wide eyes", designLanguage = "Loud & Energetic",
            designPrinciples = "Visibility, energy, openness", designBreakdown = "Wide, round eyes and a generally softer jawline communicate his friendly and exuberant personality.", description = "A boisterous ninja whose design screams for attention and reflects his warm heart.",
            profile = VisualAxes(
                faceLength = 0.38f, jawSharpness = 0.4f, eyeNarrowness = 0.15f,
                angularity = 0.35f, symmetry = 0.7f, warmth = 0.9f,
                browWeight = 0.6f, expressionNeutrality = 0.2f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "tanjiro", name = "Tanjiro", series = "Demon Slayer", designer = "Akira Matsushima", franchise = "Demon Slayer",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Scar, large kind eyes, earrings", designLanguage = "Compassionate Resolve",
            designPrinciples = "Warmth, tragedy, kindness", designBreakdown = "Very large, softly shaped eyes are the focal point, instantly conveying his deep empathy.", description = "A kind-hearted boy driven to cure his sister, visually defined by warmth and determination.",
            profile = VisualAxes(
                faceLength = 0.4f, jawSharpness = 0.45f, eyeNarrowness = 0.1f,
                angularity = 0.4f, symmetry = 0.8f, warmth = 0.95f,
                browWeight = 0.55f, expressionNeutrality = 0.3f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "edward", name = "Edward", series = "Fullmetal Alchemist", designer = "Hiromu Arakawa", franchise = "FMA",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Golden eyes, braid, automail", designLanguage = "Stubborn Genius",
            designPrinciples = "Youthful defiance, alchemical gold", designBreakdown = "A slightly rounded, youthful face heavily contrasted by sharp, angry eyes conveying his temper.", description = "A brilliant but short-tempered alchemist, balancing youth with heavy burdens.",
            profile = VisualAxes(
                faceLength = 0.35f, jawSharpness = 0.5f, eyeNarrowness = 0.3f,
                angularity = 0.5f, symmetry = 0.75f, warmth = 0.6f,
                browWeight = 0.7f, expressionNeutrality = 0.4f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "gon", name = "Gon", series = "Hunter x Hunter", designer = "Takahiro Yoshimatsu", franchise = "HxH",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Spiky green hair, massive round eyes", designLanguage = "Innocent Explorer",
            designPrinciples = "Childlike wonder, raw potential", designBreakdown = "Extremely large, circular eyes and a round face maximize the impression of innocence and youth.", description = "An innocent and endlessly curious boy whose design reflects boundless energy.",
            profile = VisualAxes(
                faceLength = 0.3f, jawSharpness = 0.35f, eyeNarrowness = 0.05f,
                angularity = 0.3f, symmetry = 0.85f, warmth = 0.9f,
                browWeight = 0.4f, expressionNeutrality = 0.2f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "bakugo", name = "Bakugo", series = "My Hero Academia", designer = "Yoshihiko Umakoshi", franchise = "MHA",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Explosive hair, aggressive triangular eyes", designLanguage = "Volatile Aggression",
            designPrinciples = "Explosions translated into facial geometry", designBreakdown = "Everything from his hair to his incredibly sharp, slanted eyes screams aggression and danger.", description = "A furiously angry hero-in-training whose entire design language is based on sharp, explosive shapes.",
            profile = VisualAxes(
                faceLength = 0.42f, jawSharpness = 0.65f, eyeNarrowness = 0.5f,
                angularity = 0.85f, symmetry = 0.7f, warmth = 0.15f,
                browWeight = 0.9f, expressionNeutrality = 0.1f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "killua", name = "Killua", series = "Hunter x Hunter", designer = "Takahiro Yoshimatsu", franchise = "HxH",
            cluster = "Cluster B - Sharp & Cool", visualTraits = "Cat-like eyes, fluffy white hair", designLanguage = "Feline Assassin",
            designPrinciples = "Deceptive cuteness, lethal sharpness", designBreakdown = "Cat-like, slightly slanted eyes on a youthful face allow him to switch instantly between cute and terrifying.", description = "A child assassin who hides his deadly skills behind a playful, feline appearance.",
            profile = VisualAxes(
                faceLength = 0.35f, jawSharpness = 0.5f, eyeNarrowness = 0.35f,
                angularity = 0.55f, symmetry = 0.8f, warmth = 0.4f,
                browWeight = 0.6f, expressionNeutrality = 0.6f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "denji", name = "Denji", series = "Chainsaw Man", designer = "Kiyotaka Oshiyama", franchise = "Chainsaw Man",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Shark teeth, manic eyes, messy blonde hair", designLanguage = "Feral Scrapper",
            designPrinciples = "Roughness, desperation, unhinged energy", designBreakdown = "Wide, manic eyes and a rough jawline perfectly capture his chaotic, unrefined energy.", description = "A destitute teenager who fights like a rabid dog, with a design reflecting his chaotic life.",
            profile = VisualAxes(
                faceLength = 0.45f, jawSharpness = 0.55f, eyeNarrowness = 0.3f,
                angularity = 0.6f, symmetry = 0.65f, warmth = 0.5f,
                browWeight = 0.55f, expressionNeutrality = 0.35f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "luffy", name = "Luffy", series = "One Piece", designer = "Kazuya Hisada", franchise = "One Piece",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Straw hat, scar under eye, massive smile", designLanguage = "Elastic Joy",
            designPrinciples = "Freedom, rubber-like flexibility", designBreakdown = "A round face and massive, simple eyes allow for extreme, cartoonish expressions of joy and anger.", description = "A pirate who embodies pure freedom and adventure, reflected in his simple, expressive face.",
            profile = VisualAxes(
                faceLength = 0.33f, jawSharpness = 0.35f, eyeNarrowness = 0.15f,
                angularity = 0.3f, symmetry = 0.7f, warmth = 0.95f,
                browWeight = 0.45f, expressionNeutrality = 0.15f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "goku", name = "Goku", series = "Dragon Ball Z", designer = "Minoru Maeda", franchise = "Dragon Ball",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Iconic spiky hair, strong brow, soft eyes (base)", designLanguage = "Martial Artist",
            designPrinciples = "Strength, purity, progression", designBreakdown = "A blocky, strong jaw paired with relatively soft, wide eyes in his base form shows his naive but strong nature.", description = "A pure-hearted warrior whose design balances incredible muscle with a friendly face.",
            profile = VisualAxes(
                faceLength = 0.4f, jawSharpness = 0.65f, eyeNarrowness = 0.25f,
                angularity = 0.6f, symmetry = 0.85f, warmth = 0.85f,
                browWeight = 0.75f, expressionNeutrality = 0.4f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "vegeta", name = "Vegeta", series = "Dragon Ball Z", designer = "Minoru Maeda", franchise = "Dragon Ball",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Widow's peak, permanent scowl", designLanguage = "Proud Prince",
            designPrinciples = "Pride, tension, rivalry", designBreakdown = "An extremely sharp widow's peak and aggressive, angular eyes communicate pure pride and aggression.", description = "The proud Saiyan prince, defined visually by his sharp hairline and sharper scowl.",
            profile = VisualAxes(
                faceLength = 0.45f, jawSharpness = 0.7f, eyeNarrowness = 0.45f,
                angularity = 0.8f, symmetry = 0.8f, warmth = 0.15f,
                browWeight = 0.9f, expressionNeutrality = 0.2f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "anya", name = "Anya", series = "Spy x Family", designer = "Kazuaki Shimada", franchise = "Spy x Family",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Pink hair, horn ornaments, huge green eyes", designLanguage = "Meme Gremlin",
            designPrinciples = "Maximum expressiveness, comedic proportions", designBreakdown = "A tiny face dominated by massive eyes allows for an incredibly wide range of comedic expressions.", description = "A telepathic child whose massive eyes make her the ultimate source of reaction faces.",
            profile = VisualAxes(
                faceLength = 0.25f, jawSharpness = 0.2f, eyeNarrowness = 0.05f,
                angularity = 0.2f, symmetry = 0.75f, warmth = 0.9f,
                browWeight = 0.3f, expressionNeutrality = 0.2f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "power", name = "Power", series = "Chainsaw Man", designer = "Kiyotaka Oshiyama", franchise = "Chainsaw Man",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Horns, cross-shaped pupils, smug grin", designLanguage = "Arrogant Fiend",
            designPrinciples = "Chaos, superiority, unkempt nature", designBreakdown = "Sharp, predatory eyes paired with a constantly smug mouth highlight her arrogant, demonic nature.", description = "A blood fiend with a massive ego and a design that mixes demonic traits with chaotic beauty.",
            profile = VisualAxes(
                faceLength = 0.4f, jawSharpness = 0.5f, eyeNarrowness = 0.3f,
                angularity = 0.55f, symmetry = 0.8f, warmth = 0.4f,
                browWeight = 0.5f, expressionNeutrality = 0.3f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "sailor-moon", name = "Sailor Moon", series = "Sailor Moon", designer = "Kazuko Tadano", franchise = "Sailor Moon",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Odango hair, massive blue eyes", designLanguage = "Magical Girl Progenitor",
            designPrinciples = "Shoujo romance, lunar motifs, pure emotion", designBreakdown = "Incredibly large, sparkling eyes and a tiny, delicate jawline set the standard for 90s magical girls.", description = "The quintessential magical girl, featuring delicate shoujo proportions and massive, emotional eyes.",
            profile = VisualAxes(
                faceLength = 0.35f, jawSharpness = 0.3f, eyeNarrowness = 0.1f,
                angularity = 0.25f, symmetry = 0.85f, warmth = 0.8f,
                browWeight = 0.3f, expressionNeutrality = 0.3f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "ainz", name = "Ainz", series = "Overlord", designer = "Takahiro Yoshimatsu", franchise = "Overlord",
            cluster = "Cluster E - Non Human / Fantasy", visualTraits = "Skeletal face, glowing red orbs for eyes", designLanguage = "Undead Overlord",
            designPrinciples = "Intimidation, lack of flesh, supreme power", designBreakdown = "The literal absence of flesh and skin creates a completely rigid, terrifying skeletal visage.", description = "An undead sorcerer king whose skeletal design is inherently intimidating and emotionless.",
            profile = VisualAxes(
                faceLength = 0.7f, jawSharpness = 0.9f, eyeNarrowness = 0.8f,
                angularity = 0.95f, symmetry = 0.95f, warmth = 0.05f,
                browWeight = 0.9f, expressionNeutrality = 0.95f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "rimuru", name = "Rimuru", series = "Slime Isekai", designer = "Ryoma Ebata", franchise = "TenSura",
            cluster = "Cluster E - Non Human / Fantasy", visualTraits = "Androgynous, soft slime-like features, blue hair", designLanguage = "Androgynous Slime",
            designPrinciples = "Fluidity, harmless appearance, hidden power", designBreakdown = "Extremely soft, rounded facial contours visually communicate his fluid, slime-based nature.", description = "A reincarnated slime whose human form is soft, androgynous, and highly approachable.",
            profile = VisualAxes(
                faceLength = 0.35f, jawSharpness = 0.25f, eyeNarrowness = 0.15f,
                angularity = 0.2f, symmetry = 0.9f, warmth = 0.85f,
                browWeight = 0.35f, expressionNeutrality = 0.6f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "kaneki", name = "Kaneki (Ghoul)", series = "Tokyo Ghoul", designer = "Kazuhiro Miwa", franchise = "Tokyo Ghoul",
            cluster = "Cluster E - Non Human / Fantasy", visualTraits = "One red eye, leather mask with zipper teeth", designLanguage = "Tragic Monster",
            designPrinciples = "Duality, restraint, madness", designBreakdown = "The mask obscures his humanity, while the single visible ghoul eye screams tragedy and danger.", description = "A half-ghoul whose iconic masked design represents his struggle between two worlds.",
            profile = VisualAxes(
                faceLength = 0.45f, jawSharpness = 0.6f, eyeNarrowness = 0.5f,
                angularity = 0.65f, symmetry = 0.5f, warmth = 0.15f,
                browWeight = 0.7f, expressionNeutrality = 0.5f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "batman-masked", name = "Batman (Masked)", series = "Batman", designer = "Bruce Timm", franchise = "DC",
            cluster = "Cluster C - Mature & Structured", visualTraits = "Pointed ears, white out eyes, massive square jaw", designLanguage = "Dark Knight",
            designPrinciples = "Shadow, fear, immovable justice", designBreakdown = "An incredibly massive, blocky jaw dominates the lower face, conveying absolute, immovable physical strength.", description = "A vigilante whose cowl is designed to strike fear, leaving only a grim, square jaw visible.",
            profile = VisualAxes(
                faceLength = 0.65f, jawSharpness = 0.85f, eyeNarrowness = 0.7f,
                angularity = 0.9f, symmetry = 0.95f, warmth = 0.1f,
                browWeight = 0.95f, expressionNeutrality = 0.9f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
        CharacterEntity(
            id = "spider-man-masked", name = "Spider-Man", series = "Spider-Man", designer = "Steve Ditko", franchise = "Marvel",
            cluster = "Cluster D - Highly Stylized", visualTraits = "Full face mask, massive expressive white eyes", designLanguage = "Friendly Neighborhood",
            designPrinciples = "Anonymity, agility, bug-like eyes", designBreakdown = "The completely featureless face is offset by massive, emotive lenses that act as giant, stylized eyes.", description = "A completely masked hero whose massive, distinct eye lenses allow for unexpected expressiveness.",
            profile = VisualAxes(
                faceLength = 0.5f, jawSharpness = 0.45f, eyeNarrowness = 0.2f,
                angularity = 0.4f, symmetry = 1.0f, warmth = 0.7f,
                browWeight = 0.5f, expressionNeutrality = 0.5f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),
    )
}
