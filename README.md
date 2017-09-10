# Oreberries

## Basics

### What does this mod do?
Tinkers' Construct added oreberries in 1.7.10, but they dropped this feature
when they moved to newer versions of Minecraft. This mod brings back oreberries
and their associated content, and makes them much more configurable than they
used to be.

### How do I use this mod?
You need Minecraft Forge installed first. Once that's done, just drop
oreberries-*version*.jar in your Minecraft instance's mods/ directory and,
optionally, configure it to taste.

### What settings does this mod have?
This mod has two configuration files: oreberries.json, where types of
oreberries are defined, and oreberries.cfg, where you can control growth
chances, bonemeal ability, and tinker villagers.

### How do I add/change oreberries?
Edit your oreberries.json file. See [the oreberries.json page on the wiki](https://github.com/josephcsible/Oreberries/wiki/oreberries.json)
for detailed instructions.

### Why does Minecraft crash on startup after I edited oreberries.json?
You may have made a mistake while editing oreberries.json. A syntax error in
that file will prevent Minecraft from starting. If you don't see the error, and
the crash log doesn't help you find it, try validating the contents of
oreberries.json with a tool like [JSONLint.com](https://jsonlint.com).

## Development

### How do I compile this mod from source?
You need a JDK installed first. Start a command prompt or terminal in the
directory you downloaded the source to. If you're on Windows, type
`gradlew.bat build`. Otherwise, type `./gradlew build`. Once it's done, the mod
will be saved to build/libs/oreberries-*version*.jar.

### How can I contribute to this mod's development?
Send pull requests. Note that by doing so, you agree to release your
contributions under this mod's license.

## Licensing/Permissions

### How does this mod relate to Tinkers' Construct?
This mod does not depend on Tinkers' Construct, but it does integrate with it
if present. Also, this mod uses resources from the 1.7.10 version of Tinkers'
Construct, with parts licensed under CC0 and CC-BY 3.0 (both GPL-compatible).

### What license is this released under?
It's released under the GPL v2 or later.

### Can I use this in my modpack?
Yes, even if you monetize it with adf.ly or something, and you don't need to
ask me for my permission first.
