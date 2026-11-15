package de.clinte.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ClinteClient implements ClientModInitializer {

    private static boolean hudEnabled = true;
    private static KeyMapping toggleKey;
    private static KeyMapping scanKey;
    private static KeyMapping serverModeKey;
    private static boolean serverScanAllowed = false; // nur für eigene Server, standardmäßig aus
    private static final SusChunkScanner scanner = new SusChunkScanner();

    private static final int WHITE = 0xFFFFFFFF;
    private static final int KEY_BG = 0x80000000;
    private static final int KEY_BG_ACTIVE = 0xA0FFFFFF;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("clinteclient", "main"));

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.clinteclient.toggle_hud", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, category));
        scanKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.clinteclient.scan_chunks", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K, category));
        serverModeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.clinteclient.server_mode", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_L, category));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (toggleKey.consumeClick()) hudEnabled = !hudEnabled;
            while (serverModeKey.consumeClick()) {
                serverScanAllowed = !serverScanAllowed;
                if (!scanAllowed(mc)) scanner.stop();
            }
            while (scanKey.consumeClick()) {
                if (scanAllowed(mc)) scanner.start(mc);
            }
            if (!scanAllowed(mc)) scanner.stop();
            else if (scanner.isScanning()) scanner.tick(mc);
        });

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath("clinteclient", "hud"),
                (g, delta) -> render(g));
    }

    private static boolean isSingleplayer(Minecraft mc) {
        return mc.getSingleplayerServer() != null;
    }

    /** Einzelspieler immer; auf Servern nur, wenn du es mit [L] ausdrücklich eingeschaltet hast. */
    private static boolean scanAllowed(Minecraft mc) {
        return mc.level != null && (isSingleplayer(mc) || serverScanAllowed);
    }

    private void render(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (!hudEnabled || mc.player == null || mc.options.hideGui) return;

        int y = 4;
        g.drawString(mc.font, "Clinte Client", 4, y, 0xFF55FFFF, true);
        y += 12;
        g.drawString(mc.font, mc.getFps() + " FPS", 4, y, WHITE, true);
        y += 10;
        g.drawString(mc.font, String.format("XYZ: %.1f / %.1f / %.1f",
                mc.player.getX(), mc.player.getY(), mc.player.getZ()), 4, y, WHITE, true);
        y += 10;
        g.drawString(mc.font, "Richtung: " + mc.player.getDirection().getName(), 4, y, WHITE, true);
        y += 10;
        int cx = mc.player.blockPosition().getX() >> 4;
        int cz = mc.player.blockPosition().getZ() >> 4;
        g.drawString(mc.font, "Chunk: " + cx + ", " + cz, 4, y, WHITE, true);
        y += 10;

        if (!isSingleplayer(mc)) {
            g.drawString(mc.font, serverScanAllowed ? "Server-Scan: AN [L]" : "Server-Scan: aus [L]",
                    4, y, serverScanAllowed ? 0xFFFFFF55 : 0xFFAAAAAA, true);
            y += 10;
        }

        if (scanAllowed(mc)) {
            if (scanner.isScanning()) {
                g.drawString(mc.font, "Sus-Scan läuft " + scanner.progress(), 4, y, 0xFFFFFF55, true);
                y += 10;
            } else {
                g.drawString(mc.font, "[K] Sus-Chunks suchen", 4, y, 0xFFAAAAAA, true);
                y += 10;
            }
            if (scanner.count() > 0) {
                g.drawString(mc.font, "Sus-Chunks: " + scanner.count(), 4, y, 0xFFFF5555, true);
                y += 10;
                for (SusChunkScanner.Hit h : scanner.nearest(mc, 5)) {
                    g.drawString(mc.font, String.format("  Chunk %d, %d  (Block %d, %d)  [%d]",
                            h.cx(), h.cz(), h.cx() * 16 + 8, h.cz() * 16 + 8, h.score()),
                            4, y, 0xFFFFAAAA, true);
                    y += 10;
                }
            }
        }

        drawKeystrokes(g, mc);
    }

    private void drawKeystrokes(GuiGraphics g, Minecraft mc) {
        int size = 20, gap = 2;
        int baseX = 8;
        int baseY = mc.getWindow().getGuiScaledHeight() - 90;

        key(g, mc, "W", baseX + size + gap, baseY, size, size, mc.options.keyUp.isDown());
        key(g, mc, "A", baseX, baseY + size + gap, size, size, mc.options.keyLeft.isDown());
        key(g, mc, "S", baseX + size + gap, baseY + size + gap, size, size, mc.options.keyDown.isDown());
        key(g, mc, "D", baseX + 2 * (size + gap), baseY + size + gap, size, size, mc.options.keyRight.isDown());
        key(g, mc, "LMB", baseX, baseY + 2 * (size + gap), size + size / 2 + 1, size, mc.options.keyAttack.isDown());
        key(g, mc, "RMB", baseX + size + size / 2 + gap + 1, baseY + 2 * (size + gap),
                size + size / 2 + 1, size, mc.options.keyUse.isDown());
        key(g, mc, "SPACE", baseX, baseY + 3 * (size + gap), 3 * size + 2 * gap, 14, mc.options.keyJump.isDown());
    }

    private void key(GuiGraphics g, Minecraft mc, String label, int x, int y, int w, int h, boolean down) {
        g.fill(x, y, x + w, y + h, down ? KEY_BG_ACTIVE : KEY_BG);
        int color = down ? 0xFF000000 : WHITE;
        int tx = x + (w - mc.font.width(label)) / 2;
        int ty = y + (h - 8) / 2;
        g.drawString(mc.font, label, tx, ty, color, false);
    }
}
