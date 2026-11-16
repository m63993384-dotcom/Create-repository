package de.clinte.client;

/** Alle Schalter des Clients. Werden im Menü (Rechte Shift-Taste) umgestellt. */
public final class Settings {
    private Settings() {}

    public static boolean hud = true;
    public static boolean fps = true;
    public static boolean coords = true;
    public static boolean chunkInfo = true;
    public static boolean keystrokes = true;

    public static boolean fullbright = false;
    public static boolean autoSprint = false;

    /** Sus-Chunk-Scan auf Servern (in Einzelspieler immer möglich). */
    public static boolean serverScan = false;
}
