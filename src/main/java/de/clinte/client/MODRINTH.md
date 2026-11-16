# Modrinth-Veröffentlichung: Vorlage

## Projekt-Einstellungen
- Name: Claud Clinte
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
Claud Clinte is a lightweight client-side Fabric mod with an in-game menu and a configurable HUD.

**Features**
- Menu with toggles for every module (open with Right Shift)
- HUD: FPS, coordinates, facing direction, current chunk, keystrokes
- Fullbright (night vision effect, client-side only)
- Auto-Sprint
- Chunk scanner: lists loaded chunks with many player-built blocks (planks, doors, beds, chests, torches, glass ...). Always available in singleplayer; on servers it is OFF by default and must be enabled in the menu. Please only use it where it is allowed.

**Keys** (changeable in Controls)
- Right Shift: open menu
- K: scan chunks

Requires Fabric API. Minecraft 1.21.11.
