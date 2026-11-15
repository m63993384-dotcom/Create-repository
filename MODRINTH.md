# Modrinth-Veröffentlichung: Vorlage

## Projekt-Einstellungen
- Name: Clinte Client
- Kurzbeschreibung (Summary): Client-side HUD with FPS, coordinates, chunk info, keystrokes and an optional chunk scanner.
- Project type: Mod
- Loader: Fabric
- Game version: 1.21.11
- Environment: Client = required, Server = unsupported
- Kategorien: Utility, Game mechanics (optional)
- License: MIT (LICENSE-Datei liegt bei)
- Source: Link zu deinem GitHub-Repository
- Icon: PNG, quadratisch (z. B. 512x512)

## Version hochladen
- Version number: 1.0.0
- Datei: build/libs/clinte-client-1.0.0.jar  (NICHT die *-sources.jar und NICHT die ZIP aus GitHub Actions)
- Dependencies: Fabric API (required)
- Version type: Release (oder Beta zum Testen)

## Beschreibung (Beispieltext, Englisch)
Clinte Client is a lightweight client-side Fabric mod that adds a configurable HUD.

**Features**
- FPS, coordinates, facing direction and current chunk
- Keystrokes display (WASD, mouse buttons, space)
- Chunk scanner: lists loaded chunks with many player-built blocks (planks, doors, beds, chests, torches, glass ...). Enabled in singleplayer; on servers it is OFF by default and must be enabled manually. Please only use it where it is allowed.

**Keys** (changeable in Controls)
- Right Shift: toggle HUD
- K: scan chunks
- L: allow chunk scan on servers

Requires Fabric API. Minecraft 1.21.11.
