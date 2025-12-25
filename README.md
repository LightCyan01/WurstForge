# WurstForge

A fork of [ForgeWurst](https://github.com/Wurst-Imperium/ForgeWurst)

> [!WARNING]  
Some functionality may not work as intended and will likely not be fixed or updated regularly. Use at your own risk.

## About

WurstForge is a Minecraft 1.12.2 hacked client built on Forge, forked from the original [ForgeWurst](https://github.com/Wurst-Imperium/ForgeWurst). This version includes additional features, optimizations, and modifications.

## Key Changes

- Ported most features from Wurst
- Added select features from other open-source clients
- 100+ total features available

## Installation

1. Install Minecraft 1.12.2 with Forge
2. Download the [latest release JAR](https://github.com/LightCyan01/WurstForge/releases)
3. Place in your `.minecraft/mods` folder
4. Launch Minecraft

## How to Compile

Requirements:
- JDK 8 or higher (JDK 25 recommended for building)

Steps:
```bash
git clone https://github.com/LightCyan01/WurstForge.git
cd WurstForge
./gradlew build
```

The compiled JAR will be in `build/libs/forgewurst-0.12.jar`

## FAQ

**Q: How do I open the menu?**  
A: Press RSHIFT to open the ClickGUI

**Q: How do I set keybinds?**  
A: Middle-click on any hack in the ClickGUI to set a keybind

**Q: How do I remove a keybind?**  
A: Right-click on the hack to remove its keybind

**Q: How do I use commands?**  
A: Commands use the `.` prefix (e.g., `.help`, `.t`)

**Q: Will this be updated?**  
A: This is a personal fork and may not receive regular updates

## Credits

- Original ForgeWurst by Alexander01998
- Fork maintained by LightCyan01

## License

This project inherits the GPL-3.0 license from ForgeWurst.
