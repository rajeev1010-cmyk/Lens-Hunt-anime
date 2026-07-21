import re

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "r") as f:
    content = f.read()

replacement = """                            Text(char.series, style = MaterialTheme.typography.labelLarge)
                            if (char.archetype.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Archetype: ${char.archetype}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))"""

content = content.replace('                            Text(char.series, style = MaterialTheme.typography.labelLarge)\n                            \n                            Spacer(modifier = Modifier.height(16.dp))', replacement)

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "w") as f:
    f.write(content)
