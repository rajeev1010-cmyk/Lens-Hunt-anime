import re
with open("merge_seed.py", "r") as f:
    content = f.read()

new_content = content.replace(
    'designBreakdown = "{clean(design_breakdown)}",\n            description = "{clean(c[\'description\'])}",\n            profile = VisualAxes(',
    'designBreakdown = "{clean(design_breakdown)}",\n            description = "{clean(c[\'description\'])}",\n            calling = "{c.get(\'giantverseArchetype\', {}).get(\'calling\', \'Hunter\')}",\n            archetype = "{c.get(\'giantverseArchetype\', {}).get(\'archetype\', \'Pathfinder\')}",\n            profile = VisualAxes('
)

with open("merge_seed.py", "w") as f:
    f.write(new_content)
