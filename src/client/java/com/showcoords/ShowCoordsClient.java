package com.showcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class ShowCoordsClient implements ClientModInitializer {
	public static final String MODID = "showcoords";

	@Override
	public void onInitializeClient() {
		// Attached after the last vanilla element so the text is drawn on top of everything.
		// Like the vanilla elements, it is hidden together with the HUD (F1).
		HudElementRegistry.attachElementAfter(
				VanillaHudElements.SUBTITLES,
				Identifier.fromNamespaceAndPath(MODID, "coords"),
				ShowCoordsClient::extractRenderState
		);
	}

	private static void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.player == null || mc.debugEntries.isOverlayVisible()) {
			return;
		}

		Player player = mc.player;

		int x = (int) player.getX();
		int y = (int) player.getY();
		int z = (int) player.getZ();

		String coordText = String.format("XYZ: %d / %d / %d", x, y, z);
		graphics.text(mc.font, coordText, 5, 5, 0xFFFFFFFF);

		graphics.text(mc.font, getBiomeName(player), 5, 15, 0xFFAAAAAA);
	}

	private static String getBiomeName(Player player) {
		return player.level().getBiome(player.blockPosition()).unwrapKey()
				.map(key -> "Biome: " + formatBiomeName(key.identifier().getPath()))
				.orElse("Biome: Unknown");
	}

	private static String formatBiomeName(String biome) {
		StringBuilder formatted = new StringBuilder();

		for (String word : biome.split("_")) {
			if (word.isEmpty()) {
				continue;
			}
			if (formatted.length() > 0) {
				formatted.append(" ");
			}
			formatted.append(word.substring(0, 1).toUpperCase())
					.append(word.substring(1).toLowerCase());
		}

		return formatted.toString();
	}
}
