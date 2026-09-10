package net.satisfy.vinery.client.render.block.storage;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class StorageRenderData {
    private BlockModelRenderState[] blockModels = new BlockModelRenderState[0];
    private ItemStackRenderState[] itemModels = new ItemStackRenderState[0];
    private ItemStack[] itemStacks = new ItemStack[0];
    private int size;

    public void ensureSize(int size) {
        if (blockModels.length < size) {
            int oldSize = blockModels.length;

            blockModels = Arrays.copyOf(blockModels, size);
            itemModels = Arrays.copyOf(itemModels, size);
            itemStacks = Arrays.copyOf(itemStacks, size);

            for (int i = oldSize; i < size; i++) {
                blockModels[i] = new BlockModelRenderState();
                itemModels[i] = new ItemStackRenderState();
                itemStacks[i] = ItemStack.EMPTY;
            }
        }

        this.size = size;
    }

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public void setStack(int slot, ItemStack stack) {
        itemStacks[slot] = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    public ItemStack getStack(int slot) {
        if (slot < 0 || slot >= size) {
            return ItemStack.EMPTY;
        }

        return itemStacks[slot];
    }

    public BlockModelRenderState getBlockModel(int slot) {
        return blockModels[slot];
    }

    public ItemStackRenderState getItemModel(int slot) {
        return itemModels[slot];
    }
}