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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WinemakerHelmetItem extends WinemakerArmorItem {
    private final Identifier hatTexture;

    public WinemakerHelmetItem(Supplier<ArmorMaterial> armorMaterial, ArmorType type, Properties properties, Identifier hatTexture) {
        super(armorMaterial, type, properties);
        this.hatTexture = hatTexture;
    }

    public Identifier getHatTexture() {
        return hatTexture;
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return super.getEquipmentSlot();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext ctx, TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag context) {
        if (Minecraft.getInstance().level!= null && Minecraft.getInstance().level.isClientSide()) {
            List<Component> components = new ArrayList<>();
            ArmorRegistryClient.appendToolTip(components);
            components.forEach(tooltip);
        }
    }
}
