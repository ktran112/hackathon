package hackathon.qolmods.ui;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InventorySorter {
    public static final int HOTBAR_SIZE = 9;
    public static final int INVENTORY_MAX_COUNT = 36;

    public void groupInventory(PlayerEntity player) {
        Map<Item, ArrayList<ItemStack>> groupedMap = CreateFilledGroupMap(player);
        ArrayList<Map.Entry<Item, ArrayList<ItemStack>>> sortedEntries = sortInventory(groupedMap);
        groupInventory(player, sortedEntries);

        if (!player.getEntityWorld().isClient()) {
            player.playerScreenHandler.syncState();
        }
    }

    private Map<Item, ArrayList<ItemStack>> CreateFilledGroupMap(PlayerEntity player) {
        Map<Item, ArrayList<ItemStack>> grouped = new HashMap<>();
        PlayerInventory inventory = player.getInventory();

        for (int i = HOTBAR_SIZE; i < INVENTORY_MAX_COUNT; i++) {
            ItemStack stack = inventory.getStack(i);

            if(!stack.isEmpty()) {
                Item item = stack.getItem();
                grouped.computeIfAbsent(item, k -> new ArrayList<>()).add(stack);
            }
        }
        return grouped;
    }

    private ArrayList<Map.Entry<Item, ArrayList<ItemStack>>> sortInventory(Map<Item, ArrayList<ItemStack>> grouped) {
        ArrayList<Map.Entry<Item, ArrayList<ItemStack>>> sortedEntries = new ArrayList<>(grouped.entrySet());
        sortedEntries.sort((a, b) -> {
            Item itemA = a.getKey();
            Item itemB = b.getKey();

            int priorityA = getItemSortPriority(itemA);
            int priorityB = getItemSortPriority(itemB);

            if (priorityA != priorityB) {
                return Integer.compare(priorityA, priorityB);
            }

            return itemA.toString().compareTo(itemB.toString());
        });

        return sortedEntries;
    }


    private void groupInventory(PlayerEntity player, ArrayList<Map.Entry<Item, ArrayList<ItemStack>>> sortedEntries) {
        int newInventoryIndex = HOTBAR_SIZE;
        PlayerInventory inventory = player.getInventory();
        ItemStack[] newInventory = new ItemStack[INVENTORY_MAX_COUNT];

        Arrays.fill(newInventory, ItemStack.EMPTY);

        for (int i = 0; i < HOTBAR_SIZE; i++) {
            newInventory[i] = inventory.getStack(i);
        }

        for (Map.Entry<Item, ArrayList<ItemStack>> entry: sortedEntries) {
            ArrayList<ItemStack> stacks = entry.getValue();
            int maxStackSize = stacks.get(0).getMaxCount();
            Item item = stacks.get(0).getItem();
            if (maxStackSize == 1) {
                for (ItemStack stack : stacks) {
                    if (newInventoryIndex < INVENTORY_MAX_COUNT) {
                        newInventory[newInventoryIndex] = stack.copy();
                        newInventoryIndex++;
                    }
                }
            } else {
                int totalCount = 0;

                for (ItemStack stack : stacks) {
                    totalCount += stack.getCount();
                }

                while (totalCount > 0 && newInventoryIndex < INVENTORY_MAX_COUNT) {
                    int stackSize = Math.min(maxStackSize, totalCount);
                    ItemStack merged = new ItemStack(item, stackSize);
                    newInventory[newInventoryIndex] = merged;
                    newInventoryIndex++;
                    totalCount -= stackSize;
                }
            }
        }
        for (int i = 0; i < newInventory.length; i++) {
            inventory.setStack(i, newInventory[i]);
        }
    }



    private int getItemSortPriority(Item item) {
        ItemStack stack = item.getDefaultStack();

        if (stack.isIn(ItemTags.PICKAXES)) return 1;
        if (stack.isIn(ItemTags.AXES)) return 2;
        if (stack.isIn(ItemTags.SHOVELS)) return 3;
        if (stack.isIn(ItemTags.HOES)) return 4;
        if (stack.isIn(ItemTags.SWORDS)) return 5;

        if (stack.isIn(ItemTags.ARROWS)) return 6;
        if (stack.isIn(ItemTags.HEAD_ARMOR)) return 7;
        if (stack.isIn(ItemTags.CHEST_ARMOR)) return 8;
        if (stack.isIn(ItemTags.LEG_ARMOR)) return 9;
        if (stack.isIn(ItemTags.FOOT_ARMOR)) return 10;

        if (stack.getComponents().contains(DataComponentTypes.FOOD)) return 11;

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            String name = Registries.BLOCK.getId(block).getPath();

            if (name.contains("stone") || name.contains("plank") ||
                    name.contains("brick")) return 12;

            if (stack.isIn(ItemTags.LOGS) || stack.isIn(ItemTags.PLANKS)) return 13;
            if (name.contains("dirt") || name.contains("grass")) return 14;

            return 15;
        }

        return 99;
    }
}
