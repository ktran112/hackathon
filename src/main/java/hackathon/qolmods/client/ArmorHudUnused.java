package hackathon.qolmods.client;


import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EquipmentSlot;


public class ArmorHudUnused {
    public static final Identifier HUD_ID = Identifier.of("qolmods", "armour_hud");


    public static void register() {
        System.out.println("[ArmourHPHud] Registering HUD..."); // add this

// Adds the HUD element
        // drawContext draws the hud using int variables
        // Tick counter updates the HP every tick
        // addLast applies the hud
        // HUD_ID is an Identifier object, and it's used to identify something from the game
        HudElementRegistry.addLast(HUD_ID, (drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            // Literally if player is not existing in the client, then no hud rendering.
            if (client.player == null) {
                return;
            }

            EquipmentSlot[] armourSlots = new EquipmentSlot[]{
                    EquipmentSlot.HEAD,
                    EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.FEET
            };

            int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
            int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

            // Positions element right side next to hotbar
            //int x = screenWidth / 2 + 101;
            //int y = screenHeight - 17;
            int x = 10;
            int y = 10;

            // Responsible for rendering specifically the text
            TextRenderer textRenderer = client.textRenderer;


            // Getting value for the screen size (to find optimal spot for hud element)


            // Represents a stack of items. Has data such as: the item count and the stack's components.
            // client.player.getInventory().armor receives data from the players armor
            // armourPiece is just referring to a particular piece of armour depending on the context of
            // how its being used.
            for (EquipmentSlot slot : armourSlots) {

                // This represents the actual piece of armour (such as a helmet, chestplate, leggings and boots)
                ItemStack armourPiece = client.player.getEquippedStack(slot);

                // This corrects the hud element position when a piece of armour is missing
                if (armourPiece == null || armourPiece.isEmpty()) {
                    y += 12;
                    continue;
                }

                // Fetches the max HP of said piece of armour
                int maxHP = armourPiece.getMaxDamage();

                // Fetches the damage the piece of armour has received
                int dmg = armourPiece.getDamage();

                // This is the current HP of the armour
                int currentHP = maxHP - dmg;

                // This is the HP in terms of percentage.
                float percentHP = ((float) currentHP / maxHP);

                // This code is for the colour of the HP
                int colourHP;
                if (percentHP > 0.5f) {
                    colourHP = 0x9AD19D;
                } else if (percentHP > 0.25f) {
                    colourHP = 0xFFEB85;
                } else if (percentHP > 0.05f) {
                    colourHP = 0x9A0000;
                } else {
                    colourHP = 0X430E21;
                }

                // getName() returns the name of the itemStack object (armourPiece in this case)
                // getString() makes it a String instead of ItemStack object

                String armourString = armourPiece.getName().getString();
                String displayedArmourString = armourString + ": " + currentHP + "/" + maxHP;

                drawContext.drawTextWithShadow(textRenderer, Text.literal(displayedArmourString), x, y, colourHP);
                y += 12;
            }
        });


    }
}
