# Claud Clinte (Fabric, Minecraft 1.21.11)

## Bauen
Voraussetzungen: JDK 21 und Gradle (oder den Gradle-Wrapper aus der Fabric-Example-Mod kopieren).

    gradle wrapper --gradle-version latest
    ./gradlew build

Die fertige .jar liegt in `build/libs/clinte-client-1.0.0.jar`.
Mit `./gradlew runClient` startest du Minecraft direkt zum Testen.

## Installieren
Fabric Loader + Fabric API für 1.21.11 installieren, .jar in den `mods`-Ordner legen.

## Bedienung
Rechte Shift-Taste: Menü öffnen (alle Module an/aus). K: Sus-Chunks suchen.

## .jar automatisch bauen lassen (ohne lokales Gradle)
1. Projekt in ein neues GitHub-Repository hochladen.
2. Tab "Actions" -> Workflow "build" läuft automatisch.
3. Nach ca. 2-5 Minuten: Artifact "clinte-client-jar" herunterladen, die .jar daraus in den mods-Ordner legen.

## Sus-Chunks (nur Einzelspieler)
Taste K: scannt die Chunks im Umkreis von 10 Chunks und listet die mit den meisten
künstlichen Blöcken (Bretter, Türen, Betten, Truhen, Fackeln, Glas ...) im HUD.
Schwellwert und Radius: Konstanten in SusChunkScanner.java. Dörfer können ebenfalls auftauchen.

## Auf deinem eigenen Server
Taste L schaltet den Chunk-Scan auf Servern ein/aus (standardmäßig aus). Danach wie gewohnt K zum Scannen.
Nur auf Servern nutzen, auf denen das erlaubt ist (z. B. dein eigener Server mit Wissen der Mitspieler).
Gescannt werden nur Chunks, die dein Client geladen hat (Server-Renderdistanz begrenzt das).
