package net.satisfy.vinery.core.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.util.function.Supplier;

public class DarkCherryBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> WOOD_TYPE =
            SynchedEntityData.defineId(
                    DarkCherryBoatEntity.class,
                    EntityDataSerializers.INT
            );

    public DarkCherryBoatEntity(
            EntityType<? extends Boat> type,
            Level level
    ) {
        super(
                type,
                level,
                DarkCherryBoatEntity.Type.DARK_CHERRY.getItem()
        );

        this.blocksBuilding = true;
    }

    public DarkCherryBoatEntity(
            Level level,
            double x,
            double y,
            double z
    ) {
        this(EntityTypeRegistry.DARK_CHERRY_BOAT.get(), level);

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
                Type.DARK_CHERRY.ordinal()
        );
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        this.setWoodType(
                Type.byName(
                        input.getStringOr(
                                "Type",
                                Type.DARK_CHERRY.getName()
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

    public Type getWoodType() {
        return Type.byId(this.entityData.get(WOOD_TYPE));
    }

    public void setWoodType(Type type) {
        this.entityData.set(WOOD_TYPE, type.ordinal());
    }

    public enum Type {
        DARK_CHERRY(
                "dark_cherry",
                ObjectRegistry.DARK_CHERRY_BOAT,
                ObjectRegistry.DARK_CHERRY_CHEST_BOAT
        );

        private final String name;
        private final Supplier<Item> item;
        private final Supplier<Item> chestItem;

        Type(
                String name,
                Supplier<Item> boatItem,
                Supplier<Item> chestBoatItem
        ) {
            this.name = name;
            this.item = boatItem;
            this.chestItem = chestBoatItem;
        }

        public Identifier getTexture(boolean hasChest) {
            return Identifier.fromNamespaceAndPath(
                    Vinery.MOD_ID,
                    hasChest
                            ? "textures/entity/chest_boat/" + this.name + ".png"
                            : "textures/entity/boat/" + this.name + ".png"
            );
        }

        public String getModelLocation() {
            return "boat/" + this.name;
        }

        public String getChestModelLocation() {
            return "chest_boat/" + this.name;
        }

        public String getName() {
            return this.name;
        }

        public Supplier<Item> getItem() {
            return this.item;
        }

        public Supplier<Item> getChestItem() {
            return this.chestItem;
        }

        public static Type byId(int id) {
            Type[] values = values();

            if (id < 0 || id >= values.length) {
                return DARK_CHERRY;
            }

            return values[id];
        }

        public static Type byName(String name) {
            for (Type value : values()) {
                if (value.name.equals(name)) {
                    return value;
                }
            }

            return DARK_CHERRY;
        }
    }
}