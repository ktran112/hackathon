package hackathon.qolmods.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ArmorHud implements ClientModInitializer {

    public void onInitializeClient() {
        System.out.println("[QoLMods] ArmorHud initialized.");

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) {
                return;
            }

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            // Match health bar’s center alignment, just below it
            int xPos = 10;
            int yPos = screenHeight - 64;



            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            ItemStack chest  = client.player.getEquippedStack(EquipmentSlot.CHEST);
            ItemStack legs   = client.player.getEquippedStack(EquipmentSlot.LEGS);
            ItemStack boots  = client.player.getEquippedStack(EquipmentSlot.FEET);

            ItemStack[] armor = {helmet, chest, legs, boots};

            for (ItemStack stack : armor) {
                if (!stack.isEmpty() && stack.isDamageable()) {
                    int maxHP = stack.getMaxDamage();
                    int dmg = stack.getDamage();
                    int currentHP = maxHP - dmg;
                    // This is the HP in terms of percentage.
                    float percentHP = ((float) currentHP / maxHP);

                    drawContext.drawItem(stack, xPos, yPos);

                    // This code is for the colour of the HP
                    int colourHP;
                    if (percentHP > 0.5f) {
                        colourHP = 0xFF9AD19D;
                    } else if (percentHP > 0.25f) {
                        colourHP = 0xFFFFEB85;
                    } else if (percentHP > 0.05f) {
                        colourHP = 0xFF9A0000;
                    } else {
                        colourHP = 0xFF430E21;
                    }

                    String displayHP = currentHP + "/" + maxHP;
                    drawContext.drawText(client.textRenderer, Text.literal(displayHP), xPos + 20, yPos + 5, colourHP, true);

                    yPos += 16;
                }
            }



        });
    }
}

