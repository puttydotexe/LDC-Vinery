package net.satisfy.vinery.core.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;
import java.util.Optional;

public class FoodComponent {

    private final int nutrition;
    private final float saturationModifier;
    private final boolean canAlwaysEat;
    private final float eatSeconds;
    private final Optional<ItemStack> usingConvertsTo;
    private final List<PossibleEffect> effects;
    private final FoodProperties foodProperties;
    private final Consumable consumable;

    public static final Codec<PossibleEffect> POSSIBLE_EFFECT_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            MobEffectInstance.CODEC.fieldOf("effect").forGetter(PossibleEffect::effect),
            Codec.FLOAT.optionalFieldOf("probability", 1.0F).forGetter(PossibleEffect::probability)
        ).apply(instance, PossibleEffect::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PossibleEffect> POSSIBLE_EFFECT_STREAM_CODEC = StreamCodec.composite(
        MobEffectInstance.STREAM_CODEC, PossibleEffect::effect,
        ByteBufCodecs.FLOAT, PossibleEffect::probability,
        PossibleEffect::new
    );

    public static final Codec<FoodComponent> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("nutrition").forGetter(FoodComponent::nutrition),
            Codec.FLOAT.fieldOf("saturation").forGetter(FoodComponent::saturationModifier),
            Codec.BOOL.optionalFieldOf("can_always_eat", false).forGetter(FoodComponent::canAlwaysEat),
            Codec.FLOAT.optionalFieldOf("eat_seconds", 1.6F).forGetter(FoodComponent::eatSeconds),
            ItemStack.OPTIONAL_CODEC.optionalFieldOf("using_converts_to").forGetter(FoodComponent::usingConvertsTo),
            POSSIBLE_EFFECT_CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FoodComponent::getEffects)
        ).apply(instance, FoodComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FoodComponent> DIRECT_STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, FoodComponent::nutrition,
        ByteBufCodecs.FLOAT, FoodComponent::saturationModifier,
        ByteBufCodecs.BOOL, FoodComponent::canAlwaysEat,
        ByteBufCodecs.FLOAT, FoodComponent::eatSeconds,
        ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional), FoodComponent::usingConvertsTo,
        POSSIBLE_EFFECT_STREAM_CODEC.apply(ByteBufCodecs.list()), FoodComponent::getEffects,
        FoodComponent::new
    );

    public FoodComponent(int nutrition, float saturationModifier, boolean canAlwaysEat, float eatSeconds, Optional<ItemStack> usingConvertsTo, List<PossibleEffect> effects) {
        this.nutrition = nutrition;
        this.saturationModifier = saturationModifier;
        this.canAlwaysEat = canAlwaysEat;
        this.eatSeconds = eatSeconds;
        this.usingConvertsTo = usingConvertsTo;
        this.effects = effects;
        this.foodProperties = new FoodProperties(nutrition, saturationModifier, canAlwaysEat);

        Consumable.Builder builder = Consumable.builder()
            .consumeSeconds(eatSeconds)
            .animation(ItemUseAnimation.EAT);

        effects.forEach(effect ->
            builder.onConsume(new ApplyStatusEffectsConsumeEffect(effect.effect(), effect.probability()))
        );
    
        this.consumable = builder.build();
    }

    public FoodComponent(List<PossibleEffect> statusEffects) {
        this(1, 0.0f, true, 1.6F, Optional.empty(), statusEffects);
    }

    public FoodComponent(int nutrition, float saturationModifier, boolean canAlwaysEat, boolean fastFood, boolean meat, List<PossibleEffect> statusEffects) {
        this(nutrition, saturationModifier, canAlwaysEat, fastFood ? 0.8F : 1.6F, Optional.empty(), statusEffects);
    }

    public List<PossibleEffect> getEffects() {
        return effects;
    }

    public FoodProperties getFoodProperties() {
        return foodProperties;
    }

    public Consumable getConsumable() {
        return consumable;
    }

    public int nutrition() {
        return nutrition;
    }

    public int getNutrition() {
        return nutrition;
    }

    public float saturationModifier() {
        return saturationModifier;
    }

    public float getSaturationModifier() {
        return saturationModifier;
    }

    public boolean canAlwaysEat() {
        return canAlwaysEat;
    }

    public float eatSeconds() {
        return eatSeconds;
    }

    public Optional<ItemStack> usingConvertsTo() {
        return usingConvertsTo;
    }

    public boolean isFastFood() {
        return eatSeconds < 1.6F;
    }

    public record PossibleEffect(MobEffectInstance effect, float probability) {}
}
