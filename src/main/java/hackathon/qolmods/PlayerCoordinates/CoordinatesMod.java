package hackathon.qolmods.PlayerCoordinates;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CoordinatesMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("[QoLMods] CoordinatesMod initialized.");

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            double x = client.player.getX();
            double y = client.player.getY();
            double z = client.player.getZ();

            String currentBiome = client.world.getBiome(client.player.getBlockPos())
                    .getKey()
                    .map(k -> k.getValue().getPath())
                    .orElse("unknown");

            String coordsText = String.format("XYZ: %.1f / %.1f / %.1f (%s)", x, y, z, currentBiome);

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            // Match health bar’s center alignment, just below it
            int rectWidth = 220;
            int rectHeight = 20;
            int xPos = (screenWidth - rectWidth) / 2;
            int yPos = (int) (screenHeight / 1.275);

            //dark background box
            drawContext.fill(xPos, yPos, xPos + rectWidth, yPos + rectHeight, 0x88000000);

            // Center the text in the box
            int textX = xPos + (rectWidth / 2) - (client.textRenderer.getWidth(coordsText) / 2);
            int textY = yPos + (rectHeight / 2) - 4;

            drawContext.drawText(
                    client.textRenderer,
                    coordsText,
                    textX,
                    textY,
                    0xFF00FF00,
                    true
            );
        });
    }
}
