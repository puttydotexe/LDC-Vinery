package net.satisfy.vinery.core.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.core.util.FoodComponent;
import net.satisfy.vinery.core.util.WineYears;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void applyFoodEffects(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (!stack.has(DataComponentRegistry.CUSTOM_FOOD.get())) {
            return;
        }

        FoodComponent foodComponent = stack.get(DataComponentRegistry.CUSTOM_FOOD.get());
        if (foodComponent == null) {
            return;
        }

        List<FoodComponent.PossibleEffect> effects = foodComponent.getEffects();
        for (FoodComponent.PossibleEffect effect : effects) {
            if (level.isClientSide() || effect.effect() == null || !(level.getRandom().nextFloat() < effect.probability())) {
                continue;
            }

            MobEffectInstance baseEffect = effect.effect();
            int duration = baseEffect.is(MobEffects.INSTANT_HEALTH) || baseEffect.is(MobEffects.INSTANT_DAMAGE) ? 1 : baseEffect.getDuration();
            livingEntity.addEffect(new MobEffectInstance(baseEffect.getEffect(), duration, WineYears.getEffectLevel(stack, level)));
        }
    }
}
