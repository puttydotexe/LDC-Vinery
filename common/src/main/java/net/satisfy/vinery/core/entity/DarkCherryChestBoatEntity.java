package net.satisfy.vinery.core.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;

public class DarkCherryChestBoatEntity extends ChestBoat {
    private static final EntityDataAccessor<Integer> WOOD_TYPE =
            SynchedEntityData.defineId(
                    DarkCherryChestBoatEntity.class,
                    EntityDataSerializers.INT
            );

    public DarkCherryChestBoatEntity(
            EntityType<? extends ChestBoat> entityType,
            Level level
    ) {
        super(
                entityType,
                level,
                DarkCherryBoatEntity.Type.DARK_CHERRY.getChestItem()
        );
    }

    public DarkCherryChestBoatEntity(
            Level level,
            double x,
            double y,
            double z
    ) {
        this(
                EntityTypeRegistry.DARK_CHERRY_CHEST_BOAT.get(),
                level
        );

        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(
                WOOD_TYPE,
                DarkCherryBoatEntity.Type.DARK_CHERRY.ordinal()
        );
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        this.setWoodType(
                DarkCherryBoatEntity.Type.byName(
                        input.getStringOr(
                                "Type",
                                DarkCherryBoatEntity.Type.DARK_CHERRY.getName()
                        )
                )
        );
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        output.putString(
                "Type",
                this.getWoodType().getName()
        );
    }

    public DarkCherryBoatEntity.Type getWoodType() {
        return DarkCherryBoatEntity.Type.byId(
                this.entityData.get(WOOD_TYPE)
        );
    }

    public void setWoodType(DarkCherryBoatEntity.Type type) {
        this.entityData.set(WOOD_TYPE, type.ordinal());
    }

    public DarkCherryBoatEntity.Type getModVariant() {
        return this.getWoodType();
    }
}