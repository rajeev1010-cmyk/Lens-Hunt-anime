import json

characters = [
    {
        "id": "levi", "name": "Levi", "series": "Attack on Titan", "designer": "Kyoji Asano", "franchise": "AoT", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Narrow eyes, undercut, stern brow, sharp jawline",
        "designLanguage": "Stoic Guardian",
        "designPrinciples": "Minimalism, Sharpness, Utility over expression",
        "designBreakdown": "Narrow eye shapes communicate intense focus, while the sharp jaw provides a sense of mature composure.",
        "description": "A hyper-competent combatant characterized by a serious, immovable expression.",
        "faceLength": 0.38, "jawSharpness": 0.65, "eyeNarrowness": 0.70, "angularity": 0.75, "symmetry": 0.85, "warmth": 0.20, "browWeight": 0.70, "expressionNeutrality": 0.90
    },
    {
        "id": "mikasa", "name": "Mikasa", "series": "Attack on Titan", "designer": "Kyoji Asano", "franchise": "AoT", "cluster": "Cluster A - Clean & Stoic",
        "visualTraits": "Dark hair, calm gaze, subtle angularity",
        "designLanguage": "Reserved Protector",
        "designPrinciples": "Quiet strength, subtle melancholy",
        "designBreakdown": "Slightly softer jawline than Levi, but with equally intense and narrow eyes that communicate resolve.",
        "description": "Fiercely loyal and highly capable, her design emphasizes quiet sorrow and strength.",
        "faceLength": 0.40, "jawSharpness": 0.55, "eyeNarrowness": 0.50, "angularity": 0.60, "symmetry": 0.80, "warmth": 0.30, "browWeight": 0.60, "expressionNeutrality": 0.80
    },
    {
        "id": "violet", "name": "Violet", "series": "Violet Evergarden", "designer": "Akiko Takase", "franchise": "Kyoto Animation", "cluster": "Cluster A - Clean & Stoic",
        "visualTraits": "Large expressive eyes (later), mechanical precision initially, soft color palette",
        "designLanguage": "Elegant Automaton",
        "designPrinciples": "Beauty in mechanical precision turning into emotional warmth",
        "designBreakdown": "Perfect symmetry and classical beauty ratios create an almost doll-like initial impression.",
        "description": "An ex-soldier learning to understand human emotions, her design reflects a beautiful but tragic doll.",
        "faceLength": 0.42, "jawSharpness": 0.45, "eyeNarrowness": 0.30, "angularity": 0.40, "symmetry": 0.95, "warmth": 0.40, "browWeight": 0.40, "expressionNeutrality": 0.75
    },
    {
        "id": "loid", "name": "Loid", "series": "Spy x Family", "designer": "Kazuaki Shimada", "franchise": "Spy x Family", "cluster": "Cluster C - Mature & Structured",
        "visualTraits": "Sharp features, blonde hair, composed expression",
        "designLanguage": "Suave Professional",
        "designPrinciples": "Deceptive perfection, classical masculine elegance",
        "designBreakdown": "Strong, straight nose and defined jawline create a classically handsome and trustworthy appearance.",
        "description": "A master spy whose appearance is carefully constructed to be impeccably perfect and unreadable.",
        "faceLength": 0.45, "jawSharpness": 0.60, "eyeNarrowness": 0.45, "angularity": 0.65, "symmetry": 0.90, "warmth": 0.50, "browWeight": 0.65, "expressionNeutrality": 0.80
    },
    {
        "id": "yor", "name": "Yor", "series": "Spy x Family", "designer": "Kazuaki Shimada", "franchise": "Spy x Family", "cluster": "Cluster A - Clean & Stoic",
        "visualTraits": "Soft face but sharp eyes in combat, black hair, gentle default smile",
        "designLanguage": "Hidden Assassin",
        "designPrinciples": "Contrast between soft domesticity and sharp lethality",
        "designBreakdown": "Soft, rounded facial contours contrast with incredibly sharp, focused eye designs when serious.",
        "description": "A deadly assassin posing as a mild-mannered clerk, featuring dual design modes.",
        "faceLength": 0.38, "jawSharpness": 0.40, "eyeNarrowness": 0.35, "angularity": 0.45, "symmetry": 0.85, "warmth": 0.70, "browWeight": 0.45, "expressionNeutrality": 0.50
    },
    {
        "id": "spike", "name": "Spike", "series": "Cowboy Bebop", "designer": "Toshihiro Kawamoto", "franchise": "Cowboy Bebop", "cluster": "Cluster C - Mature & Structured",
        "visualTraits": "Tall, lanky, relaxed features, messy hair",
        "designLanguage": "Languid Cool",
        "designPrinciples": "Effortless style, underlying tragedy",
        "designBreakdown": "Long face and slightly drooping eyes create a permanently relaxed, unbothered expression.",
        "description": "A space cowboy with a dark past, hiding his skills behind a slacker facade.",
        "faceLength": 0.50, "jawSharpness": 0.55, "eyeNarrowness": 0.55, "angularity": 0.60, "symmetry": 0.70, "warmth": 0.40, "browWeight": 0.55, "expressionNeutrality": 0.75
    },
    {
        "id": "rukia", "name": "Rukia", "series": "Bleach", "designer": "Tite Kubo", "franchise": "Bleach", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Large sharp eyes, angular jaw, petite frame",
        "designLanguage": "Fierce Elegance",
        "designPrinciples": "Sharpness, nobility, intense gaze",
        "designBreakdown": "Highly angular eyes paired with a sharp, small chin emphasize her intense and serious nature.",
        "description": "A noble Soul Reaper whose strict demeanor is matched by her sharp visual design.",
        "faceLength": 0.35, "jawSharpness": 0.60, "eyeNarrowness": 0.25, "angularity": 0.70, "symmetry": 0.80, "warmth": 0.30, "browWeight": 0.60, "expressionNeutrality": 0.65
    },
    {
        "id": "eren", "name": "Eren", "series": "Attack on Titan", "designer": "Kyoji Asano", "franchise": "AoT", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Fierce glare, strong brows, messy hair",
        "designLanguage": "Aggressive Drive",
        "designPrinciples": "Intensity, anger, forward momentum",
        "designBreakdown": "Heavy, lowered brows and intensely focused eyes convey unrelenting determination and anger.",
        "description": "Driven by a singular, violent goal, his facial features reflect raw emotional intensity.",
        "faceLength": 0.42, "jawSharpness": 0.60, "eyeNarrowness": 0.40, "angularity": 0.65, "symmetry": 0.75, "warmth": 0.20, "browWeight": 0.85, "expressionNeutrality": 0.40
    },
    {
        "id": "gojo", "name": "Gojo", "series": "Jujutsu Kaisen", "designer": "Tadashi Hiramatsu", "franchise": "JJK", "cluster": "Cluster C - Mature & Structured",
        "visualTraits": "Blindfold, white hair, very tall, sharp jaw",
        "designLanguage": "Untouchable Arrogance",
        "designPrinciples": "Divine confidence, obscured vision",
        "designBreakdown": "The lower half of his face is sharply defined, contrasting with his covered eyes to create mystique.",
        "description": "The strongest sorcerer, incredibly arrogant but deeply caring.",
        "faceLength": 0.48, "jawSharpness": 0.70, "eyeNarrowness": 0.50, "angularity": 0.65, "symmetry": 0.95, "warmth": 0.50, "browWeight": 0.50, "expressionNeutrality": 0.60
    },
    {
        "id": "sasuke", "name": "Sasuke", "series": "Naruto", "designer": "Tetsuya Nishio", "franchise": "Naruto", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Dark features, sharp eyes, spiky back hair",
        "designLanguage": "Avenger's Edge",
        "designPrinciples": "Coolness, tragedy, sharpness",
        "designBreakdown": "Very sharp, angled eyes and a prominent brow emphasize his continuous state of anger and focus.",
        "description": "A prodigy driven by revenge, with a design that screams 'cool and unapproachable'.",
        "faceLength": 0.40, "jawSharpness": 0.65, "eyeNarrowness": 0.45, "angularity": 0.70, "symmetry": 0.85, "warmth": 0.20, "browWeight": 0.75, "expressionNeutrality": 0.80
    },
    {
        "id": "sung-jinwoo", "name": "Sung Jinwoo", "series": "Solo Leveling", "designer": "Tomoko Sudo", "franchise": "Solo Leveling", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Glowing eyes, sharp jaw, sleek hair",
        "designLanguage": "Shadow Monarch",
        "designPrinciples": "Transformation, overwhelming power, edge",
        "designBreakdown": "Extremely sharp V-shaped jaw and narrow, glowing eyes give him an imposing, predator-like aesthetic.",
        "description": "A weak hunter who transformed into an unstoppable, edgy powerhouse.",
        "faceLength": 0.44, "jawSharpness": 0.75, "eyeNarrowness": 0.60, "angularity": 0.80, "symmetry": 0.90, "warmth": 0.10, "browWeight": 0.80, "expressionNeutrality": 0.85
    },
    {
        "id": "cloud", "name": "Cloud", "series": "Final Fantasy VII", "designer": "Tetsuya Nomura", "franchise": "Final Fantasy", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Spiky blonde hair, glowing eyes, sharp chin",
        "designLanguage": "Fractured Soldier",
        "designPrinciples": "Iconic silhouette, cold exterior",
        "designBreakdown": "Sharp angular features combined with large, intense eyes reflect his strong but broken persona.",
        "description": "A mercenary with a complex past and an iconic, sharp-edged design.",
        "faceLength": 0.42, "jawSharpness": 0.60, "eyeNarrowness": 0.40, "angularity": 0.65, "symmetry": 0.85, "warmth": 0.30, "browWeight": 0.65, "expressionNeutrality": 0.75
    },
    {
        "id": "2b", "name": "2B", "series": "NieR:Automata", "designer": "Akihiko Yoshida", "franchise": "NieR", "cluster": "Cluster A - Clean & Stoic",
        "visualTraits": "Blindfold, silver bob, pale skin",
        "designLanguage": "Gothic Android",
        "designPrinciples": "Elegance, lack of vision, tragic beauty",
        "designBreakdown": "A delicate, rounded chin contrasts heavily with her combat-ready attire and obscured eyes.",
        "description": "A combat android whose beautiful, delicate lower face belies her lethal capabilities.",
        "faceLength": 0.38, "jawSharpness": 0.45, "eyeNarrowness": 0.50, "angularity": 0.40, "symmetry": 0.95, "warmth": 0.20, "browWeight": 0.50, "expressionNeutrality": 0.90
    },
    {
        "id": "makima", "name": "Makima", "series": "Chainsaw Man", "designer": "Kiyotaka Oshiyama", "franchise": "Chainsaw Man", "cluster": "Cluster C - Mature & Structured",
        "visualTraits": "Ringed eyes, soft smile, business attire",
        "designLanguage": "Predatory Calm",
        "designPrinciples": "Unnerving stillness, false warmth",
        "designBreakdown": "Perfectly symmetrical, calm features paired with hypnotic ringed eyes create an intensely unnerving aura.",
        "description": "A high-ranking devil hunter whose serene beauty masks total control and ruthlessness.",
        "faceLength": 0.40, "jawSharpness": 0.50, "eyeNarrowness": 0.35, "angularity": 0.45, "symmetry": 0.98, "warmth": 0.60, "browWeight": 0.40, "expressionNeutrality": 0.85
    },
    {
        "id": "ichigo", "name": "Ichigo", "series": "Bleach", "designer": "Tite Kubo", "franchise": "Bleach", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Orange hair, permanent scowl, sharp eyes",
        "designLanguage": "Defiant Punk",
        "designPrinciples": "Rebellion, sharpness, protective instinct",
        "designBreakdown": "A constant frown and sharp, angular eyes give him an aggressive, unapproachable resting face.",
        "description": "A hot-headed teenager with a strong sense of duty and a sharp, iconic design.",
        "faceLength": 0.45, "jawSharpness": 0.65, "eyeNarrowness": 0.45, "angularity": 0.70, "symmetry": 0.75, "warmth": 0.40, "browWeight": 0.80, "expressionNeutrality": 0.30
    },
    {
        "id": "naruto", "name": "Naruto", "series": "Naruto", "designer": "Tetsuya Nishio", "franchise": "Naruto", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Whisker marks, bright blonde hair, wide eyes",
        "designLanguage": "Loud & Energetic",
        "designPrinciples": "Visibility, energy, openness",
        "designBreakdown": "Wide, round eyes and a generally softer jawline communicate his friendly and exuberant personality.",
        "description": "A boisterous ninja whose design screams for attention and reflects his warm heart.",
        "faceLength": 0.38, "jawSharpness": 0.40, "eyeNarrowness": 0.15, "angularity": 0.35, "symmetry": 0.70, "warmth": 0.90, "browWeight": 0.60, "expressionNeutrality": 0.20
    },
    {
        "id": "tanjiro", "name": "Tanjiro", "series": "Demon Slayer", "designer": "Akira Matsushima", "franchise": "Demon Slayer", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Scar, large kind eyes, earrings",
        "designLanguage": "Compassionate Resolve",
        "designPrinciples": "Warmth, tragedy, kindness",
        "designBreakdown": "Very large, softly shaped eyes are the focal point, instantly conveying his deep empathy.",
        "description": "A kind-hearted boy driven to cure his sister, visually defined by warmth and determination.",
        "faceLength": 0.40, "jawSharpness": 0.45, "eyeNarrowness": 0.10, "angularity": 0.40, "symmetry": 0.80, "warmth": 0.95, "browWeight": 0.55, "expressionNeutrality": 0.30
    },
    {
        "id": "edward", "name": "Edward", "series": "Fullmetal Alchemist", "designer": "Hiromu Arakawa", "franchise": "FMA", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Golden eyes, braid, automail",
        "designLanguage": "Stubborn Genius",
        "designPrinciples": "Youthful defiance, alchemical gold",
        "designBreakdown": "A slightly rounded, youthful face heavily contrasted by sharp, angry eyes conveying his temper.",
        "description": "A brilliant but short-tempered alchemist, balancing youth with heavy burdens.",
        "faceLength": 0.35, "jawSharpness": 0.50, "eyeNarrowness": 0.30, "angularity": 0.50, "symmetry": 0.75, "warmth": 0.60, "browWeight": 0.70, "expressionNeutrality": 0.40
    },
    {
        "id": "gon", "name": "Gon", "series": "Hunter x Hunter", "designer": "Takahiro Yoshimatsu", "franchise": "HxH", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Spiky green hair, massive round eyes",
        "designLanguage": "Innocent Explorer",
        "designPrinciples": "Childlike wonder, raw potential",
        "designBreakdown": "Extremely large, circular eyes and a round face maximize the impression of innocence and youth.",
        "description": "An innocent and endlessly curious boy whose design reflects boundless energy.",
        "faceLength": 0.30, "jawSharpness": 0.35, "eyeNarrowness": 0.05, "angularity": 0.30, "symmetry": 0.85, "warmth": 0.90, "browWeight": 0.40, "expressionNeutrality": 0.20
    },
    {
        "id": "bakugo", "name": "Bakugo", "series": "My Hero Academia", "designer": "Yoshihiko Umakoshi", "franchise": "MHA", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Explosive hair, aggressive triangular eyes",
        "designLanguage": "Volatile Aggression",
        "designPrinciples": "Explosions translated into facial geometry",
        "designBreakdown": "Everything from his hair to his incredibly sharp, slanted eyes screams aggression and danger.",
        "description": "A furiously angry hero-in-training whose entire design language is based on sharp, explosive shapes.",
        "faceLength": 0.42, "jawSharpness": 0.65, "eyeNarrowness": 0.50, "angularity": 0.85, "symmetry": 0.70, "warmth": 0.15, "browWeight": 0.90, "expressionNeutrality": 0.10
    },
    {
        "id": "killua", "name": "Killua", "series": "Hunter x Hunter", "designer": "Takahiro Yoshimatsu", "franchise": "HxH", "cluster": "Cluster B - Sharp & Cool",
        "visualTraits": "Cat-like eyes, fluffy white hair",
        "designLanguage": "Feline Assassin",
        "designPrinciples": "Deceptive cuteness, lethal sharpness",
        "designBreakdown": "Cat-like, slightly slanted eyes on a youthful face allow him to switch instantly between cute and terrifying.",
        "description": "A child assassin who hides his deadly skills behind a playful, feline appearance.",
        "faceLength": 0.35, "jawSharpness": 0.50, "eyeNarrowness": 0.35, "angularity": 0.55, "symmetry": 0.80, "warmth": 0.40, "browWeight": 0.60, "expressionNeutrality": 0.60
    },
    {
        "id": "denji", "name": "Denji", "series": "Chainsaw Man", "designer": "Kiyotaka Oshiyama", "franchise": "Chainsaw Man", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Shark teeth, manic eyes, messy blonde hair",
        "designLanguage": "Feral Scrapper",
        "designPrinciples": "Roughness, desperation, unhinged energy",
        "designBreakdown": "Wide, manic eyes and a rough jawline perfectly capture his chaotic, unrefined energy.",
        "description": "A destitute teenager who fights like a rabid dog, with a design reflecting his chaotic life.",
        "faceLength": 0.45, "jawSharpness": 0.55, "eyeNarrowness": 0.30, "angularity": 0.60, "symmetry": 0.65, "warmth": 0.50, "browWeight": 0.55, "expressionNeutrality": 0.35
    },
    {
        "id": "luffy", "name": "Luffy", "series": "One Piece", "designer": "Kazuya Hisada", "franchise": "One Piece", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Straw hat, scar under eye, massive smile",
        "designLanguage": "Elastic Joy",
        "designPrinciples": "Freedom, rubber-like flexibility",
        "designBreakdown": "A round face and massive, simple eyes allow for extreme, cartoonish expressions of joy and anger.",
        "description": "A pirate who embodies pure freedom and adventure, reflected in his simple, expressive face.",
        "faceLength": 0.33, "jawSharpness": 0.35, "eyeNarrowness": 0.15, "angularity": 0.30, "symmetry": 0.70, "warmth": 0.95, "browWeight": 0.45, "expressionNeutrality": 0.15
    },
    {
        "id": "goku", "name": "Goku", "series": "Dragon Ball Z", "designer": "Minoru Maeda", "franchise": "Dragon Ball", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Iconic spiky hair, strong brow, soft eyes (base)",
        "designLanguage": "Martial Artist",
        "designPrinciples": "Strength, purity, progression",
        "designBreakdown": "A blocky, strong jaw paired with relatively soft, wide eyes in his base form shows his naive but strong nature.",
        "description": "A pure-hearted warrior whose design balances incredible muscle with a friendly face.",
        "faceLength": 0.40, "jawSharpness": 0.65, "eyeNarrowness": 0.25, "angularity": 0.60, "symmetry": 0.85, "warmth": 0.85, "browWeight": 0.75, "expressionNeutrality": 0.40
    },
    {
        "id": "vegeta", "name": "Vegeta", "series": "Dragon Ball Z", "designer": "Minoru Maeda", "franchise": "Dragon Ball", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Widow's peak, permanent scowl",
        "designLanguage": "Proud Prince",
        "designPrinciples": "Pride, tension, rivalry",
        "designBreakdown": "An extremely sharp widow's peak and aggressive, angular eyes communicate pure pride and aggression.",
        "description": "The proud Saiyan prince, defined visually by his sharp hairline and sharper scowl.",
        "faceLength": 0.45, "jawSharpness": 0.70, "eyeNarrowness": 0.45, "angularity": 0.80, "symmetry": 0.80, "warmth": 0.15, "browWeight": 0.90, "expressionNeutrality": 0.20
    },
    {
        "id": "anya", "name": "Anya", "series": "Spy x Family", "designer": "Kazuaki Shimada", "franchise": "Spy x Family", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Pink hair, horn ornaments, huge green eyes",
        "designLanguage": "Meme Gremlin",
        "designPrinciples": "Maximum expressiveness, comedic proportions",
        "designBreakdown": "A tiny face dominated by massive eyes allows for an incredibly wide range of comedic expressions.",
        "description": "A telepathic child whose massive eyes make her the ultimate source of reaction faces.",
        "faceLength": 0.25, "jawSharpness": 0.20, "eyeNarrowness": 0.05, "angularity": 0.20, "symmetry": 0.75, "warmth": 0.90, "browWeight": 0.30, "expressionNeutrality": 0.20
    },
    {
        "id": "power", "name": "Power", "series": "Chainsaw Man", "designer": "Kiyotaka Oshiyama", "franchise": "Chainsaw Man", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Horns, cross-shaped pupils, smug grin",
        "designLanguage": "Arrogant Fiend",
        "designPrinciples": "Chaos, superiority, unkempt nature",
        "designBreakdown": "Sharp, predatory eyes paired with a constantly smug mouth highlight her arrogant, demonic nature.",
        "description": "A blood fiend with a massive ego and a design that mixes demonic traits with chaotic beauty.",
        "faceLength": 0.40, "jawSharpness": 0.50, "eyeNarrowness": 0.30, "angularity": 0.55, "symmetry": 0.80, "warmth": 0.40, "browWeight": 0.50, "expressionNeutrality": 0.30
    },
    {
        "id": "sailor-moon", "name": "Sailor Moon", "series": "Sailor Moon", "designer": "Kazuko Tadano", "franchise": "Sailor Moon", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Odango hair, massive blue eyes",
        "designLanguage": "Magical Girl Progenitor",
        "designPrinciples": "Shoujo romance, lunar motifs, pure emotion",
        "designBreakdown": "Incredibly large, sparkling eyes and a tiny, delicate jawline set the standard for 90s magical girls.",
        "description": "The quintessential magical girl, featuring delicate shoujo proportions and massive, emotional eyes.",
        "faceLength": 0.35, "jawSharpness": 0.30, "eyeNarrowness": 0.10, "angularity": 0.25, "symmetry": 0.85, "warmth": 0.80, "browWeight": 0.30, "expressionNeutrality": 0.30
    },
    {
        "id": "ainz", "name": "Ainz", "series": "Overlord", "designer": "Takahiro Yoshimatsu", "franchise": "Overlord", "cluster": "Cluster E - Non Human / Fantasy",
        "visualTraits": "Skeletal face, glowing red orbs for eyes",
        "designLanguage": "Undead Overlord",
        "designPrinciples": "Intimidation, lack of flesh, supreme power",
        "designBreakdown": "The literal absence of flesh and skin creates a completely rigid, terrifying skeletal visage.",
        "description": "An undead sorcerer king whose skeletal design is inherently intimidating and emotionless.",
        "faceLength": 0.70, "jawSharpness": 0.90, "eyeNarrowness": 0.80, "angularity": 0.95, "symmetry": 0.95, "warmth": 0.05, "browWeight": 0.90, "expressionNeutrality": 0.95
    },
    {
        "id": "rimuru", "name": "Rimuru", "series": "Slime Isekai", "designer": "Ryoma Ebata", "franchise": "TenSura", "cluster": "Cluster E - Non Human / Fantasy",
        "visualTraits": "Androgynous, soft slime-like features, blue hair",
        "designLanguage": "Androgynous Slime",
        "designPrinciples": "Fluidity, harmless appearance, hidden power",
        "designBreakdown": "Extremely soft, rounded facial contours visually communicate his fluid, slime-based nature.",
        "description": "A reincarnated slime whose human form is soft, androgynous, and highly approachable.",
        "faceLength": 0.35, "jawSharpness": 0.25, "eyeNarrowness": 0.15, "angularity": 0.20, "symmetry": 0.90, "warmth": 0.85, "browWeight": 0.35, "expressionNeutrality": 0.60
    },
    {
        "id": "kaneki", "name": "Kaneki (Ghoul)", "series": "Tokyo Ghoul", "designer": "Kazuhiro Miwa", "franchise": "Tokyo Ghoul", "cluster": "Cluster E - Non Human / Fantasy",
        "visualTraits": "One red eye, leather mask with zipper teeth",
        "designLanguage": "Tragic Monster",
        "designPrinciples": "Duality, restraint, madness",
        "designBreakdown": "The mask obscures his humanity, while the single visible ghoul eye screams tragedy and danger.",
        "description": "A half-ghoul whose iconic masked design represents his struggle between two worlds.",
        "faceLength": 0.45, "jawSharpness": 0.60, "eyeNarrowness": 0.50, "angularity": 0.65, "symmetry": 0.50, "warmth": 0.15, "browWeight": 0.70, "expressionNeutrality": 0.50
    },
    {
        "id": "batman-masked", "name": "Batman (Masked)", "series": "Batman", "designer": "Bruce Timm", "franchise": "DC", "cluster": "Cluster C - Mature & Structured",
        "visualTraits": "Pointed ears, white out eyes, massive square jaw",
        "designLanguage": "Dark Knight",
        "designPrinciples": "Shadow, fear, immovable justice",
        "designBreakdown": "An incredibly massive, blocky jaw dominates the lower face, conveying absolute, immovable physical strength.",
        "description": "A vigilante whose cowl is designed to strike fear, leaving only a grim, square jaw visible.",
        "faceLength": 0.65, "jawSharpness": 0.85, "eyeNarrowness": 0.70, "angularity": 0.90, "symmetry": 0.95, "warmth": 0.10, "browWeight": 0.95, "expressionNeutrality": 0.90
    },
    {
        "id": "spider-man-masked", "name": "Spider-Man", "series": "Spider-Man", "designer": "Steve Ditko", "franchise": "Marvel", "cluster": "Cluster D - Highly Stylized",
        "visualTraits": "Full face mask, massive expressive white eyes",
        "designLanguage": "Friendly Neighborhood",
        "designPrinciples": "Anonymity, agility, bug-like eyes",
        "designBreakdown": "The completely featureless face is offset by massive, emotive lenses that act as giant, stylized eyes.",
        "description": "A completely masked hero whose massive, distinct eye lenses allow for unexpected expressiveness.",
        "faceLength": 0.50, "jawSharpness": 0.45, "eyeNarrowness": 0.20, "angularity": 0.40, "symmetry": 1.0, "warmth": 0.70, "browWeight": 0.50, "expressionNeutrality": 0.50
    }
]

with open("app/src/main/java/com/example/data/local/CharacterDataSeed.kt", "w") as f:
    f.write("package com.example.data.local\n\n")
    f.write("import com.example.data.model.VisualAxes\n\n")
    f.write("object CharacterDataSeed {\n")
    f.write("    val characters = listOf(\n")
    for c in characters:
        f.write(f"""        CharacterEntity(
            id = "{c['id']}", name = "{c['name']}", series = "{c['series']}", designer = "{c['designer']}", franchise = "{c['franchise']}",
            cluster = "{c['cluster']}", visualTraits = "{c['visualTraits']}", designLanguage = "{c['designLanguage']}",
            designPrinciples = "{c['designPrinciples']}", designBreakdown = "{c['designBreakdown']}", description = "{c['description']}",
            profile = VisualAxes(
                faceLength = {c['faceLength']}f, jawSharpness = {c['jawSharpness']}f, eyeNarrowness = {c['eyeNarrowness']}f,
                angularity = {c['angularity']}f, symmetry = {c['symmetry']}f, warmth = {c['warmth']}f,
                browWeight = {c['browWeight']}f, expressionNeutrality = {c['expressionNeutrality']}f,
                hairDarkness = 0.5f, hairVolume = 0.5f, contrast = 0.5f, glasses = 0.0f
            )
        ),\n""")
    f.write("    )\n")
    f.write("}\n")
