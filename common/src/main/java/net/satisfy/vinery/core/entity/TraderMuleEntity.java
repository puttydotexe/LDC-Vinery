package net.satisfy.vinery.core.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TraderMuleEntity extends AbstractChestedHorse {
    private static final int DEFAULT_DESPAWN_DELAY = 47999;

    private int despawnDelay = DEFAULT_DESPAWN_DELAY;

    public TraderMuleEntity(
            EntityType<? extends TraderMuleEntity> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(
                1,
                new RunAroundLikeCrazyGoal(this, 1.0)
        );
        this.goalSelector.addGoal(
                3,
                new PanicGoal(this, 1.0)
        );
        this.goalSelector.addGoal(
                4,
                new BreedGoal(this, 1.0)
        );
        this.goalSelector.addGoal(
                5,
                new TemptGoal(
                        this,
                        1.0,
                        Ingredient.of(Items.HAY_BLOCK),
                        false
                )
        );
        this.goalSelector.addGoal(
                7,
                new WaterAvoidingRandomStrollGoal(this, 1.0)
        );
        this.goalSelector.addGoal(
                8,
                new RandomLookAroundGoal(this)
        );
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide()) {
            this.maybeDespawn();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("DespawnDelay", this.despawnDelay);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        this.despawnDelay = input.getIntOr(
                "DespawnDelay",
                DEFAULT_DESPAWN_DELAY
        );
    }

    private void maybeDespawn() {
        if (!this.canDespawn()) {
            return;
        }

        if (this.isLeashedToWanderingTrader()) {
            WanderingTrader trader =
                    (WanderingTrader) Objects.requireNonNull(
                            this.getLeashHolder()
                    );

            this.despawnDelay = trader.getDespawnDelay() - 1;
        } else {
            --this.despawnDelay;
        }

        if (this.despawnDelay <= 0) {
            this.dropLeash();
            this.discard();
        }
    }

    private boolean canDespawn() {
        return !this.isTamed()
                && !this.isLeashedToSomethingOtherThanTheWanderingTrader()
                && !this.hasExactlyOnePlayerPassenger();
    }

    private boolean isLeashedToWanderingTrader() {
        return this.getLeashHolder() instanceof WanderingWinemakerEntity;
    }

    private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
        return this.isLeashed() && !this.isLeashedToWanderingTrader();
    }

    @Override
    public TraderMuleEntity getBreedOffspring(
            ServerLevel serverLevel,
            AgeableMob otherParent
    ) {
        return EntityTypeRegistry.MULE.get().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DONKEY_AMBIENT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.DONKEY_ANGRY;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DONKEY_DEATH;
    }

    @Override
    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.DONKEY_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DONKEY_HURT;
    }
}
