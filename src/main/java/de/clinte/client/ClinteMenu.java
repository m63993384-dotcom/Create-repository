package de.clinte.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ClinteMenu extends Screen {

    private static final int W = 150, H = 20, GAP = 4;
    private int startX, startY, index;

    public ClinteMenu() {
        super(Component.literal("Clinte Client"));
    }

    @Override
    protected void init() {
        index = 0;
        startX = this.width / 2 - (W * 2 + GAP) / 2;
        startY = this.height / 2 - 70;

        toggle("HUD", () -> Settings.hud, v -> Settings.hud = v);
        toggle("FPS", () -> Settings.fps, v -> Settings.fps = v);
        toggle("Koordinaten", () -> Settings.coords, v -> Settings.coords = v);
        toggle("Chunk-Anzeige", () -> Settings.chunkInfo, v -> Settings.chunkInfo = v);
        toggle("Tastenanzeige", () -> Settings.keystrokes, v -> Settings.keystrokes = v);
        toggle("Fullbright", () -> Settings.fullbright, v -> Settings.fullbright = v);
        toggle("Auto-Sprint", () -> Settings.autoSprint, v -> Settings.autoSprint = v);
        toggle("Chunk-Scan auf Servern", () -> Settings.serverScan, v -> Settings.serverScan = v);

        addRenderableWidget(Button.builder(Component.literal("Fertig"), b -> onClose())
                .bounds(this.width / 2 - 75, startY + 4 * (H + GAP) + 10, 150, H)
                .build());
    }

    private void toggle(String name, BooleanSupplier get, Consumer<Boolean> set) {
        int x = startX + (index % 2) * (W + GAP);
        int y = startY + (index / 2) * (H + GAP);
        index++;
        Button button = Button.builder(label(name, get.getAsBoolean()), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(label(name, get.getAsBoolean()));
        }).bounds(x, y, W, H).build();
        addRenderableWidget(button);
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "AN" : "AUS"));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        g.drawCenteredString(this.font, this.title, this.width / 2, startY - 24, 0xFF55FFFF);
        g.drawCenteredString(this.font, Component.literal("K = Sus-Chunks suchen"),
                this.width / 2, startY + 4 * (H + GAP) + 40, 0xFFAAAAAA);
    }
}
