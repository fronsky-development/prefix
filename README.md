<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.20+-brightgreen?style=for-the-badge&logo=minecraft" alt="Minecraft 1.20+">
  <img src="https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17+">
  <img src="https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-blue?style=for-the-badge" alt="Spigot / Paper">
  <img src="https://img.shields.io/badge/License-All%20Rights%20Reserved-red?style=for-the-badge" alt="All Rights Reserved">
</p>

<h1 align="center">Prefix</h1>

<p align="center">
  Lightweight chat prefix and tablist management plugin for Minecraft servers.<br>
  Full control over player ranks, chat formatting, tab list sorting, and color customization - all through simple commands and YAML config files.
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#installation">Installation</a> •
  <a href="#commands">Commands</a> •
  <a href="#configuration">Configuration</a>
</p>

---

## Features

- **Chat Prefixes** - Customizable group-based prefixes with full color code support
- **Tab Prefixes** - Sorted tablist with prefixes, name colors, and weighted ordering
- **Name Colors** - Separate chat and tab name colors per group
- **Chat Colors** - Group-based chat message coloring
- **Group System** - YAML-based group management with per-player assignment
- **Tab Weights** - Control tablist sorting order per group
- **Live Reload** - Reload configuration without restarting the server
- **Player Mentions** - Bold highlighting when a player's name is mentioned in chat
- **Interactive Help** - Clickable help messages with hover tooltips and permission info

## Requirements

| | Minimum |
|-|---------|
| **Server** | Spigot or Paper **1.20+** |
| **Java** | **17** or newer |

## Installation

1. Download the latest release from [GitHub](https://github.com/fronsky-development/prefix) or [build from source](#building-from-source).
2. Place the jar in your server's `plugins/` folder.
3. Restart the server.
4. Edit `plugins/Prefix/groups.yml` and `plugins/Prefix/players.yml` to your liking.

## Commands

All commands start with `/prefix` (alias `/p`). All permissions default to **OP**.

| Command | Description | Permission |
|---------|-------------|------------|
| `/prefix` | View your prefix information | `prefix.command.prefix` |
| `/prefix <player>` | View another player's prefix info | `prefix.command.prefix.others` |
| `/prefix help` | Show the help menu | `prefix.command.prefix.help` |
| `/prefix info` | Show plugin version and author | `prefix.command.prefix.info` |
| `/prefix chat <group> <prefix>` | Set a group's chat prefix | `prefix.command.prefix.chat` |
| `/prefix tab <group> <prefix>` | Set a group's tab prefix | `prefix.command.prefix.tab` |
| `/prefix chatnamecolor <group> <color>` | Set a group's chat name color | `prefix.command.prefix.chatnamecolor` |
| `/prefix tabnamecolor <group> <color>` | Set a group's tab name color | `prefix.command.prefix.tabnamecolor` |
| `/prefix chatcolor <group> <color>` | Set a group's chat message color | `prefix.command.prefix.chatcolor` |
| `/prefix weight <group> <weight>` | Set a group's tab sort weight | `prefix.command.prefix.weight` |
| `/prefix group <player> <group>` | Assign a player to a group | `prefix.command.prefix.group` |
| `/prefix reload` | Reload all configuration files | `prefix.command.prefix.reload` |

> **Color arguments** accept a color code with or without the `&` prefix (e.g. `&a` or `a`). Only color codes are valid: `0`–`9` and `a`–`f`. Tab completion suggests the available codes.

### Permissions

| Permission | Description |
|------------|-------------|
| `prefix.command.prefix.*` | Full access |
| `prefix.command.prefix` | Base command access |
| `prefix.command.prefix.others` | View other players' prefix info |
| `prefix.command.prefix.help` | Access the help menu |
| `prefix.command.prefix.info` | View plugin info |
| `prefix.command.prefix.chat` | Modify chat prefixes |
| `prefix.command.prefix.tab` | Modify tab prefixes |
| `prefix.command.prefix.chatnamecolor` | Modify chat name colors |
| `prefix.command.prefix.tabnamecolor` | Modify tab name colors |
| `prefix.command.prefix.chatcolor` | Modify chat colors |
| `prefix.command.prefix.weight` | Modify tab weights |
| `prefix.command.prefix.group` | Assign groups to players |
| `prefix.command.prefix.reload` | Reload configuration |

## Configuration

### groups.yml

Stores group definitions with their visual properties.

```yaml
admin:
  chatPrefix: "&4[Admin]"
  tabPrefix: "&4[A]"
  chatNameColor: "&c"
  tabNameColor: "&c"
  chatColor: "&f"
  tabWeight: 0

moderator:
  chatPrefix: "&2[Mod]"
  tabPrefix: "&2[M]"
  chatNameColor: "&a"
  tabNameColor: "&a"
  chatColor: "&f"
  tabWeight: 1

default:
  chatPrefix: "&7[Member]"
  tabPrefix: "&7"
  chatNameColor: "&7"
  tabNameColor: "&7"
  chatColor: "&7"
  tabWeight: 9
```

### players.yml

Maps player UUIDs to their assigned group. Managed automatically by the plugin.

```yaml
550e8400-e29b-41d4-a716-446655440000:
  group: "admin"
```

## Building from Source

Requires Git. Uses the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) - no Gradle installation needed.

```bash
git clone https://github.com/fronsky-development/prefix.git
cd prefix
./gradlew clean build
```

The output jar will be at `build/libs/Prefix-<version>.jar`.

## Issues

Found a bug? Open an issue on [GitHub](https://github.com/fronsky-development/prefix/issues) with your Minecraft version, server software, plugin version, steps to reproduce, and any console errors.

## Contributing

Contributions are welcome! This repository is public and source-available. Before you start, please read:

- [Contributing Guidelines](CONTRIBUTING.md) — how to build, code style, and the pull request process
- [Code of Conduct](CODE_OF_CONDUCT.md)
- [Security Policy](SECURITY.md) — how to report vulnerabilities privately

> Note: Prefix is proprietary software (see [LICENSE](LICENSE)). By contributing you agree that your changes are distributed under the project license.

## License

Copyright &copy; 2025-2026 Fronsky. All Rights Reserved.

This is proprietary software. No part may be copied, modified, or distributed without permission. See [LICENSE](LICENSE) for full terms.

<p align="center">
  Made with ❤️ by <a href="https://fronsky.nl">Fronsky</a>
</p>
