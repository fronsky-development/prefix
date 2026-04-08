<p align="center">
  <img src="https://img.shields.io/badge/version-2.0.0-blue?style=for-the-badge" alt="Version">
  <img src="https://img.shields.io/badge/Minecraft-1.20+-green?style=for-the-badge" alt="Minecraft">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge" alt="Java">
  <img src="https://img.shields.io/badge/license-All%20Rights%20Reserved-red?style=for-the-badge" alt="License">
</p>

# Prefix by Fronsky

**Prefix** is a lightweight, modern chat prefix and tablist management plugin for Minecraft (Spigot/Paper) servers. It gives server owners full control over how players appear in chat and in the tablist — group-based prefixes, name colors, chat colors, and weighted tab sorting.

**Website:** [fronsky.nl/projects/prefix](https://fronsky.nl/projects/prefix)  
**Contact:** [support@fronsky.nl](mailto:support@fronsky.nl)

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Building from Source](#building-from-source)
- [Commands](#commands)
- [Permissions](#permissions)
- [Configuration](#configuration)
- [API for Developers](#api-for-developers)
- [Version Compatibility](#version-compatibility)
- [Reporting Issues](#reporting-issues)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Chat Prefixes** — Customizable group-based prefixes with full color code support.
- **Tab Prefixes** — Sorted tablist with prefixes, name colors, and weighted ordering.
- **Name Colors** — Separate chat and tab name colors per group.
- **Chat Colors** — Group-based chat message coloring.
- **Group System** — YAML-based group management with per-player assignment.
- **Tab Weights** — Control tablist sorting order per group.
- **Live Reload** — Reload configuration without restarting the server.
- **Player Mentions** — Bold highlighting when a player's name is mentioned in chat.
- **Interactive Help** — Clickable help messages with hover tooltips and permission info.

## Requirements

### Runtime

| Requirement                     | Version   |
|---------------------------------|-----------|
| Minecraft Server (Spigot/Paper) | **1.20+** |
| Java                            | **17**    |

### Build Tools

| Requirement | Version                      |
|-------------|------------------------------|
| Gradle      | 8.11+ (included via wrapper) |

## Installation

1. Download the latest release from [fronsky.nl](https://fronsky.nl/projects/prefix).
2. Place the `.jar` file in your server's `plugins/` directory.
3. Restart the server.
4. Configure settings in `plugins/Prefix/groups.yml` and `plugins/Prefix/players.yml`.

## Building from Source

This project uses the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), so no global Gradle installation is required.

```bash
# Clone the repository
git clone https://github.com/fronsky-development/prefix.git
cd prefix

# Build the plugin
./gradlew clean build
```

The output `.jar` file will be at:

```
build/libs/Prefix-<version>.jar
```

**IntelliJ IDEA setup:**
- Open the project in IntelliJ IDEA
- Verify the Project SDK matches the required Java version (`File > Project Structure > Project`)
- Verify the Gradle JVM matches (`Settings > Build > Gradle > Gradle JVM`)

## Commands

| Command                                | Description                        | Permission                            |
|----------------------------------------|------------------------------------|---------------------------------------|
| `/prefix` (alias `/p`)                | View your prefix information       | `prefix.command.prefix`               |
| `/prefix <player>`                    | View another player's prefix info  | `prefix.command.prefix.others`        |
| `/prefix help`                        | Show the help menu                 | `prefix.command.prefix.help`          |
| `/prefix info`                        | Show plugin version and author     | `prefix.command.prefix.info`          |
| `/prefix chat <group> <prefix>`       | Set a group's chat prefix          | `prefix.command.prefix.chat`          |
| `/prefix tab <group> <prefix>`        | Set a group's tab prefix           | `prefix.command.prefix.tab`           |
| `/prefix chatnamecolor <group> <color>` | Set a group's chat name color    | `prefix.command.prefix.chatnamecolor` |
| `/prefix tabnamecolor <group> <color>` | Set a group's tab name color      | `prefix.command.prefix.tabnamecolor`  |
| `/prefix chatcolor <group> <color>`   | Set a group's chat message color   | `prefix.command.prefix.chatcolor`     |
| `/prefix weight <group> <weight>`     | Set a group's tab sort weight      | `prefix.command.prefix.weight`        |
| `/prefix group <player> <group>`      | Assign a player to a group         | `prefix.command.prefix.group`         |
| `/prefix reload`                      | Reload all configuration files     | `prefix.command.prefix.reload`        |

## Permissions

All permissions default to **OP only**.

| Permission                            | Description                      |
|---------------------------------------|----------------------------------|
| `prefix.command.prefix`               | Base command access              |
| `prefix.command.prefix.*`             | Access to all subcommands        |
| `prefix.command.prefix.others`        | View other players' prefix info  |
| `prefix.command.prefix.help`          | Access the help menu             |
| `prefix.command.prefix.info`          | View plugin info                 |
| `prefix.command.prefix.chat`          | Modify chat prefixes             |
| `prefix.command.prefix.tab`           | Modify tab prefixes              |
| `prefix.command.prefix.chatnamecolor` | Modify chat name colors          |
| `prefix.command.prefix.tabnamecolor`  | Modify tab name colors           |
| `prefix.command.prefix.chatcolor`     | Modify chat colors               |
| `prefix.command.prefix.weight`        | Modify tab weights               |
| `prefix.command.prefix.group`         | Assign groups to players         |
| `prefix.command.prefix.reload`        | Reload configuration             |

Use `/prefix help` in-game for a quick reference.

## Configuration

### `groups.yml`

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

### `players.yml`

Maps player UUIDs to their assigned group. Managed automatically by the plugin.

```yaml
550e8400-e29b-41d4-a716-446655440000:
  group: "admin"
```

## API for Developers

> **🚧 Coming in v2.5.0** — A public developer API is planned for a future release.

The **PrefixAPI** will allow third-party plugins to read and modify prefix data programmatically. Planned features include:

- **Read access** — Get a player's group, prefix, suffix, and colors
- **Write access** — Change groups and prefixes programmatically
- **Custom events** — `GroupChangeEvent`, `PrefixChangeEvent` (cancellable)
- **Import/Export API** — Programmatic data migration

### Usage (planned)

```java
// Add Prefix as a compileOnly dependency in your build.gradle:
// compileOnly files('libs/Prefix-<version>.jar')

// Example usage (available from v2.5.0):
PrefixAPI api = PrefixAPI.getInstance();
PGroup group = api.getPlayerGroup(player);
String prefix = group.getChatPrefix();
```

> **Note:** The API is covered by a dedicated exception in the [LICENSE](LICENSE). Third-party plugins may freely call the public API at runtime, provided the Prefix plugin itself is not modified, redistributed, or bundled.

## Version Compatibility

The plugin is compiled against **Spigot API 1.20.6** with **Java 17** and maintains forward compatibility by:

- Avoiding NMS and CraftBukkit usage
- Using stable, non-deprecated Spigot APIs
- Relying on standard Bukkit team and scoreboard APIs for tablist functionality
- Using Java 17 language features (pattern matching, var, text blocks)

## Reporting Issues

Found a bug or have a suggestion? Please open an issue on [GitHub Issues](https://github.com/fronsky-development/prefix/issues) with:

1. **Minecraft version** and **server software** (Spigot/Paper)
2. **Plugin version** (run `/prefix info`)
3. **Steps to reproduce** the issue
4. **Console errors** (if any)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on reporting bugs, suggesting features, and submitting pull requests.

## Security

See [SECURITY.md](SECURITY.md) for information on reporting vulnerabilities.

## License

This project is proprietary software. All rights reserved.

```
Copyright © 2025-2026 Fronsky. All Rights Reserved.
```

No part of this software may be copied, modified, distributed, or used without
prior written permission from the copyright holder. Use of this software for
training machine learning or AI models, and automated scraping or data mining of
this repository, are strictly prohibited. See the [LICENSE](LICENSE) file for
full details.

Third-party dependency attributions are listed in [NOTICE](NOTICE).

<p align="center">
  Made with ❤️ by <a href="https://fronsky.nl">Fronsky</a>
</p>
