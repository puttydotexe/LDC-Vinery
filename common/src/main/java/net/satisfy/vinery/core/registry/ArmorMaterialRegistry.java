package net.satisfy.vinery.core.registry;

import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.function.Supplier;

public class ArmorMaterialRegistry {
    private static final ArmorMaterial LEATHER = ArmorMaterials.LEATHER;
    public static final Supplier<ArmorMaterial> WINEMAKER_ARMOR = () -> new ArmorMaterial(
            LEATHER.durability(),
            LEATHER.defense(),
            LEATHER.enchantmentValue(),
            LEATHER.equipSound(),
            LEATHER.toughness(),
            LEATHER.knockbackResistance(),
            LEATHER.repairIngredient(),
            EquipmentAssets.createId("winemaker")
    );
}
