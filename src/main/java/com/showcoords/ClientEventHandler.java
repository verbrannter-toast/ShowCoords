package com.showcoords;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = ShowCoords.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.getDebugOverlay().showDebugScreen()) {
            return;
        }

        Player player = mc.player;
        GuiGraphics guiGraphics = event.getGuiGraphics();

        int x = (int) player.getX();
        int y = (int) player.getY();
        int z = (int) player.getZ();

        String coordText = String.format("XYZ: %d / %d / %d", x, y, z);
        guiGraphics.drawString(mc.font, coordText, 5, 5, 0xFFFFFF);

        String biome = getBiomeName(player);
        guiGraphics.drawString(mc.font, biome, 5, 15, 0xAAAAAA);
    }

    private static String getBiomeName(Player player) {
        String biome = player.level().getBiome(player.blockPosition()).unwrapKey()
                .orElseThrow().location().toString();

        if (biome.contains(":")) {
            String[] parts = biome.split(":", 2);
            String namespace = parts[0];
            String biomeName = parts[1];

            biomeName = formatBiomeName(biomeName);

            return "Biome: " + biomeName;
        }

        return "Biome: " + biome;
    }

    private static String formatBiomeName(String biome) {
        String[] words = biome.split("_");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (formatted.length() > 0) {
                formatted.append(" ");
            }
            formatted.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase());
        }

        return formatted.toString();
    }
}