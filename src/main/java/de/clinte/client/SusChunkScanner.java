package de.clinte.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * Sucht verdächtige Chunks (viele künstliche Blöcke = wahrscheinlich Bauwerk).
 * Läuft nur in Einzelspieler-Welten. Villages/Strukturen können ebenfalls auftauchen.
 */
public class SusChunkScanner {

    public record Hit(int cx, int cz, int score) {}

    public static final int RADIUS = 10;      // Chunks um den Spieler
    public static final int THRESHOLD = 20;   // ab so vielen künstlichen Blöcken = "sus"
    private static final int CHUNKS_PER_TICK = 3;

    private final List<Hit> hits = new ArrayList<>();
    private final Deque<int[]> queue = new ArrayDeque<>();
    private int scanned = 0, total = 0;

    public boolean isScanning() { return !queue.isEmpty(); }
    public String progress() { return scanned + "/" + total; }

    public void start(Minecraft mc) {
        hits.clear();
        queue.clear();
        if (mc.player == null) return;
        int pcx = mc.player.blockPosition().getX() >> 4;
        int pcz = mc.player.blockPosition().getZ() >> 4;
        List<int[]> list = new ArrayList<>();
        for (int dx = -RADIUS; dx <= RADIUS; dx++)
            for (int dz = -RADIUS; dz <= RADIUS; dz++)
                list.add(new int[]{pcx + dx, pcz + dz});
        list.sort(Comparator.comparingInt(c -> (c[0] - pcx) * (c[0] - pcx) + (c[1] - pcz) * (c[1] - pcz)));
        queue.addAll(list);
        total = list.size();
        scanned = 0;
    }

    public void stop() { queue.clear(); hits.clear(); }

    public void tick(Minecraft mc) {
        ClientLevel level = mc.level;
        if (level == null) return;
        for (int i = 0; i < CHUNKS_PER_TICK && !queue.isEmpty(); i++) {
            int[] c = queue.poll();
            scanned++;
            ChunkAccess chunk = level.getChunk(c[0], c[1], ChunkStatus.FULL, false);
            if (chunk == null) continue;
            int score = 0;
            for (LevelChunkSection s : chunk.getSections()) {
                if (s.hasOnlyAir() || !s.maybeHas(SusChunkScanner::isArtificial)) continue;
                for (int y = 0; y < 16; y++)
                    for (int z = 0; z < 16; z++)
                        for (int x = 0; x < 16; x++)
                            if (isArtificial(s.getBlockState(x, y, z))) score++;
            }
            if (score >= THRESHOLD) hits.add(new Hit(c[0], c[1], score));
        }
    }

    /** Nächste Treffer zuerst. */
    public List<Hit> nearest(Minecraft mc, int max) {
        if (mc.player == null) return List.of();
        int pcx = mc.player.blockPosition().getX() >> 4;
        int pcz = mc.player.blockPosition().getZ() >> 4;
        return hits.stream()
                .sorted(Comparator.comparingInt(h -> (h.cx() - pcx) * (h.cx() - pcx) + (h.cz() - pcz) * (h.cz() - pcz)))
                .limit(max)
                .toList();
    }

    public int count() { return hits.size(); }

    private static boolean isArtificial(BlockState st) {
        return st.is(BlockTags.PLANKS) || st.is(BlockTags.DOORS) || st.is(BlockTags.BEDS)
                || st.is(BlockTags.WOOL) || st.is(Blocks.CHEST) || st.is(Blocks.BARREL)
                || st.is(Blocks.CRAFTING_TABLE) || st.is(Blocks.FURNACE) || st.is(Blocks.TORCH)
                || st.is(Blocks.WALL_TORCH) || st.is(Blocks.GLASS) || st.is(Blocks.GLASS_PANE)
                || st.is(Blocks.BOOKSHELF) || st.is(Blocks.BRICKS) || st.is(Blocks.STONE_BRICKS);
    }
}
