package net.satisfy.vinery.core.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.satisfy.vinery.core.registry.ArmorRegistryClient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WinemakerChestItem extends WinemakerArmorItem {
    private final Identifier chestplateTexture;

    public WinemakerChestItem(Supplier<ArmorMaterial> armorMaterial, ArmorType type, Properties properties, Identifier chestplateTexture) {
        super(armorMaterial, type, properties);
        this.chestplateTexture = chestplateTexture;
    }

    public Identifier getChestplateTexture() {
        return chestplateTexture;
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return super.getEquipmentSlot();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flag) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.isClientSide()) {
            List<Component> components = new ArrayList<>();
            ArmorRegistryClient.appendToolTip(components);
            components.forEach(tooltip);
        }
    }
}
