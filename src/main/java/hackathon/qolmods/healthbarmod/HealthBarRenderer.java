package hackathon.qolmods.healthbarmod;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.text.Text;

import java.awt.Color;

public class HealthBarRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null || client.crosshairTarget == null) return;

        if (client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) client.crosshairTarget;
            Entity entity = entityHit.getEntity();

            if (entity instanceof LivingEntity living) {
                float health = living.getHealth();
                float maxHealth = living.getMaxHealth();

                int barWidth = 100;
                int barHeight = 10;
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();
                int x = (screenWidth - barWidth) / 2;
                int y = screenHeight / 2 + 30;

                float healthPercent = health / maxHealth;
                int filledWidth = (int) (barWidth * healthPercent);
                Color color = Color.getHSBColor(healthPercent * 0.33f, 1f, 1f);

                drawContext.fill(x, y, x + barWidth, y + barHeight, 0xAA000000);
                drawContext.fill(x, y, x + filledWidth, y + barHeight, 0xFF000000 | color.getRGB());

                String healthText = String.format("%.0f / %.0f", health, maxHealth);
                drawContext.drawCenteredTextWithShadow(client.textRenderer, Text.literal(healthText),
                        x + barWidth / 2, y - 12, 0xFFFFFF);
            }
        }
    }
}
