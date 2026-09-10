package net.satisfy.vinery.core.util;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class WineSettings {
    
    private final Properties properties;
    private final int baseDuration;

    public WineSettings(Holder<MobEffect> effect, int duration, int strength) {
        this.baseDuration = duration;
        FoodProperties food = createWineFoodComponent();
        Consumable consumable = createWineConsumable(effect, duration, strength);
        this.properties = new Properties()
                .food(food, consumable);
    }

    public Properties getProperties() {
        return properties;
    }

    public int getBaseDuration() {
        return baseDuration;
    }


    private FoodProperties createWineFoodComponent() {
        return new FoodProperties(0, 0.0F, true);
    }

    private Consumable createWineConsumable(Holder<MobEffect> effect, int duration, int strength) {
        Consumable.Builder builder = Consumable.builder()
                .animation(ItemUseAnimation.DRINK);
        if (effect != null) {
            builder.onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(effect, duration, strength), 1.0f));
        }
        return builder.build();
    }
}
