package hackathon.qolmods;

import hackathon.qolmods.ui.InventorySorter;
import hackathon.qolmods.ui.ItemReplacer;
import hackathon.qolmods.ui.SortInventoryC2SPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class qolMods implements ModInitializer {
	public static final String MOD_ID = "qolmods";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ItemReplacer itemReplacer = new ItemReplacer();

        PayloadTypeRegistry.playC2S().register(SortInventoryC2SPacket.ID, SortInventoryC2SPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SortInventoryC2SPacket.ID, (payload, context) -> {
            context.server().execute(() -> {
                InventorySorter sorter = new InventorySorter();
                sorter.groupInventory(context.player());
            });
        });
        // Listen to player breaking blocks.
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            // If item is about to break, run replaceHandItem
            if (stack.willBreakNextUse()) {
                itemReplacer.replaceToolInHand(state, player);
            }

        });
    }


}