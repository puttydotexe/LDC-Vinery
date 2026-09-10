package net.satisfy.vinery.core.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class BasketItem extends BlockItem {
    public BasketItem(Block block, Properties settings) {
        super(block, new Properties().stacksTo(1));
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack, HolderLookup.Provider provider) {
        var customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return Stream.empty();

        CompoundTag compoundTag = customData.copyTag();
        CompoundTag blockEntityTag = compoundTag.getCompound("BlockEntityTag").orElse(null);
        if (blockEntityTag == null) return Stream.empty();

        ListTag itemsList = blockEntityTag.getList("Items").orElse(null);
        if (itemsList == null) return Stream.empty();

        return itemsList.stream()
                .filter(Objects::nonNull)
                .filter(tag -> tag.getId() == Tag.TAG_COMPOUND)
                .map(Tag.class::cast)
                .map(CompoundTag.class::cast)
                .map(tag -> ItemStack.OPTIONAL_CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(ItemStack.EMPTY));
    }

    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack,HolderLookup.Provider provider) {
        List<ItemStackTemplate> items = getContents(itemStack, provider)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStackTemplate::fromNonEmptyStack)
                .toList();
        return Optional.of(new BundleTooltip(new BundleContents(items)));
    }
}
