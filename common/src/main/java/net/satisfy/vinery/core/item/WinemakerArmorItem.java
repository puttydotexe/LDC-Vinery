package net.satisfy.vinery.core.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;

import java.util.function.Supplier;

public abstract class WinemakerArmorItem extends Item {
    protected final ArmorType type;

    protected WinemakerArmorItem(Supplier<ArmorMaterial> armorMaterial, ArmorType type, Properties properties) {
        super(applyArmorComponents(armorMaterial, type, properties));
        this.type = type;
    }

    private static Properties applyArmorComponents(Supplier<ArmorMaterial> armorMaterial, ArmorType type, Properties properties) {
        ArmorMaterial material = armorMaterial.get();
        return properties
                .durability(type.getDurability(material.durability()))
                .component(DataComponents.ATTRIBUTE_MODIFIERS, material.createAttributes(type))
                .component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot())
                        .setEquipSound(material.equipSound())
                        .setAsset(material.assetId())
                        .setDamageOnHurt(true)
                        .build());
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }
}
