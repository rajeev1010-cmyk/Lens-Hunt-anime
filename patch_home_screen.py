import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

new_imports = """
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
"""
if "import androidx.compose.foundation.text.KeyboardOptions" not in content:
    content = content.replace("import androidx.compose.ui.unit.dp", "import androidx.compose.ui.unit.dp\n" + new_imports)

inputs_ui = """
                    val firstName by viewModel.firstName.collectAsState()
                    val dobDay by viewModel.dobDay.collectAsState()
                    val dobMonth by viewModel.dobMonth.collectAsState()

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { viewModel.setFirstName(it) },
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = if (dobDay > 0) dobDay.toString() else "",
                            onValueChange = { viewModel.setDobDay(it.toIntOrNull() ?: 0) },
                            label = { Text("Day of Birth (1-31)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = if (dobMonth > 0) dobMonth.toString() else "",
                            onValueChange = { viewModel.setDobMonth(it.toIntOrNull() ?: 0) },
                            label = { Text("Month (1-12)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
"""

content = content.replace(
    'Text(\n                        "Target Match Filter",',
    inputs_ui + '\n                    Text(\n                        "Target Match Filter",'
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
