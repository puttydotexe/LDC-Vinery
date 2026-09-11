package net.satisfy.vinery.core.util;

import com.google.gson.JsonArray;
import com.mojang.datafixers.util.Pair;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import io.netty.buffer.Unpooled;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.entity.ChairEntity;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class GeneralUtil {

    public static final EnumProperty<LineConnectingType> LINE_CONNECTING_TYPE = EnumProperty.create("type", LineConnectingType.class);
    private static final Map<Identifier, Map<BlockPos, Pair<ChairEntity, BlockPos>>> CHAIRS = new HashMap<>();
    private static final ThreadLocal<Identifier> CURRENT_BLOCK_ID = new ThreadLocal<>();
    private static final ThreadLocal<Identifier> CURRENT_ITEM_ID = new ThreadLocal<>();

    public static RotatedPillarBlock logBlock() {
        return new RotatedPillarBlock(blockPropertiesOfFullCopy(Blocks.OAK_LOG));
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(DeferredRegister<Block> registerB, Registrar<Block> registrarB, DeferredRegister<Item> registerI, Registrar<Item> registrarI, Identifier name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = registerWithoutItem(registerB, registrarB, name, block);

        registerItem(registerI, registrarI, name, () -> new BlockItem(toReturn.get(), itemProperties()));

        return toReturn;
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(DeferredRegister<Block> register, Registrar<Block> registrar, Identifier path, Supplier<T> block) {
        Supplier<T> keyedBlock = () -> withBlockId(path, block);

        return Platform.isNeoForge() ? register.register(path.getPath(), keyedBlock) : registrar.register(path, keyedBlock);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(DeferredRegister<Item> register, Registrar<Item> registrar, Identifier path, Supplier<T> itemSupplier) {
        Supplier<T> keyedItem = () -> withItemId(path, itemSupplier);

        return Platform.isNeoForge() ? register.register(path.getPath(), keyedItem) : registrar.register(path, keyedItem);
    }

    public static BlockBehaviour.Properties blockProperties() {
        return applyCurrentBlockId(BlockBehaviour.Properties.of());
    }

    public static BlockBehaviour.Properties blockPropertiesOfFullCopy(BlockBehaviour block) {
        return applyCurrentBlockId(BlockBehaviour.Properties.ofFullCopy(block));
    }

    public static BlockBehaviour.Properties blockPropertiesOfLegacyCopy(BlockBehaviour block) {
        return applyCurrentBlockId(BlockBehaviour.Properties.ofLegacyCopy(block));
    }

    public static Item.Properties itemProperties() {
        Identifier id = CURRENT_ITEM_ID.get();
        Item.Properties properties = new Item.Properties();

        return id == null ? properties : properties.setId(ResourceKey.create(Registries.ITEM, id));
    }

    private static BlockBehaviour.Properties applyCurrentBlockId(BlockBehaviour.Properties properties) {
        Identifier id = CURRENT_BLOCK_ID.get();

        return id == null ? properties : properties.setId(ResourceKey.create(Registries.BLOCK, id));
    }

    private static <T extends Block> T withBlockId(Identifier id, Supplier<T> block) {
        CURRENT_BLOCK_ID.set(id);

        try {
            return block.get();
        } finally {
            CURRENT_BLOCK_ID.remove();
        }
    }

    private static <T extends Item> T withItemId(Identifier id, Supplier<T> item) {
        CURRENT_ITEM_ID.set(id);

        try {
            return item.get();
        } finally {
            CURRENT_ITEM_ID.remove();
        }
    }

    public static Collection<ServerPlayer> tracking(ServerLevel world, ChunkPos pos) {
        Objects.requireNonNull(world, "The world cannot be null");
        Objects.requireNonNull(pos, "The chunk pos cannot be null");

        return world.getChunkSource().chunkMap.getPlayers(pos, false);
    }

    public static Collection<ServerPlayer> tracking(ServerLevel world, BlockPos pos) {
        Objects.requireNonNull(pos, "BlockPos cannot be null");

        return tracking(world, ChunkPos.containing(pos));
    }

    public static BlockPos getPreviousPlayerPosition(Player player, ChairEntity chairEntity) {
        /* Ensure the lookup is only performed on the server. */
        if (player.level().isClientSide()) { return null; }

        Map<BlockPos, Pair<ChairEntity, BlockPos>> chairs = CHAIRS.get(getDimensionTypeId(player.level()));

        /* Ensure there are tracked chairs to search. */
        if (chairs == null) { return null; }

        /* Find the matching chair and return the player's previous position. */
        for (Pair<ChairEntity, BlockPos> pair : chairs.values()) {
            if (pair.getFirst() == chairEntity) {
                return pair.getSecond();
            }
        }

        return null;
    }

    public static InteractionResult onUse(Level world, Player player, InteractionHand hand, BlockHitResult hit, double extraHeight) {
        /* Ignore interactions that should not create a seat. */
        if (world.isClientSide() || player.isShiftKeyDown() || GeneralUtil.isPlayerSitting(player) || hit.getDirection() == Direction.DOWN || !player.getItemInHand(hand).isEmpty()) {
            return InteractionResult.PASS;
        }

        BlockPos hitPos = hit.getBlockPos();

        /* Allow normal block interaction to continue with unoccupied blocks (chairs). */
        if (GeneralUtil.isOccupied(world, hitPos)) { return InteractionResult.PASS;}

        /* Create the temporary chair entity used as the player's seat. */
        ChairEntity chair = EntityTypeRegistry.CHAIR.get().create(world, EntitySpawnReason.LOAD);

        /* Allow normal block interaction to continue if the chair entity could not be created. */
        if (chair == null) { return InteractionResult.PASS; }

        BlockState state = world.getBlockState(hitPos);

        float yaw = getSeatYaw(state);

        /* Position and rotate the chair to match the targeted block. */
        chair.setSeatPos(hitPos);
        chair.snapTo(
            hitPos.getX() + 0.5D,
            hitPos.getY() + 0.25D + extraHeight,
            hitPos.getZ() + 0.5D,
            yaw,
            0.0F
        );
        chair.yRotO = yaw;

        /* Allow normal block interaction to continue if the chair entity could not be created. */
        if (!GeneralUtil.addChairEntity(world, hitPos, chair, player.blockPosition())) { return InteractionResult.PASS; }

        world.addFreshEntity(chair);
        player.startRiding(chair);

        return InteractionResult.SUCCESS_SERVER;
    }

    private static float getSeatYaw(BlockState state) {
        float yaw = 0.0F;

        /* Match the seat rotation to the block's facing property when available. */
        for (Property<?> property : state.getProperties()) {
            /* Skip properties that do not control the block's facing direction. */
            if (!property.getName().equals("facing")) { continue; }

            if (property instanceof EnumProperty<?> enumProperty) {
                Object value = state.getValue(enumProperty);

                /* Ensure the value is a valid direction. */
                if (value instanceof Direction direction) {
                    yaw = direction.toYRot();
                }
            }

            break;
        }

        /* Reverse the seat direction when targeting the head section of a multi-part block. */
        for (Property<?> property : state.getProperties()) {
            /* Ignore properties that do not identify the block's multi-part section. */
            if (!property.getName().equals("part")) { continue; }

            /* Check whether the multi-part property identifies this block as the head section. */
            if ("head".equals(state.getValue(property).toString())) {
                yaw += 180.0F;
            }

            break;
        }

        return yaw;
    }

    public static boolean isOccupied(Level world, BlockPos pos) {
        Identifier id = getDimensionTypeId(world);

        return GeneralUtil.CHAIRS.containsKey(id) && GeneralUtil.CHAIRS.get(id).containsKey(pos);
    }

    public static boolean isPlayerSitting(Player player) {
        /* Search the chairs tracked across every dimension. */
        for (Map<BlockPos, Pair<ChairEntity, BlockPos>> chairs : CHAIRS.values()) {

            /* Check whether the player is currently riding any tracked chair. */
            for (Pair<ChairEntity, BlockPos> pair : chairs.values()) {

                /* Check whether the player is currently riding the currently iterated tracked chair. */
                if (pair.getFirst().hasPassenger(player)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static Identifier getDimensionTypeId(Level world) {
        return world.dimension().identifier();
    }

    public static void onStateReplaced(Level world, BlockPos pos) {
        /* Ensure the operation is only performed on the server. */
        if (world.isClientSide()) { return; }

        ChairEntity chair = GeneralUtil.getChairEntity(world, pos);

        /* Stop if there is no chair associated with the replaced block. */
        if (chair == null) { return; }

        GeneralUtil.removeChairEntity(world, pos);

        chair.ejectPassengers();
    }

    public static boolean addChairEntity(Level world, BlockPos blockPos, ChairEntity entity, BlockPos playerPos) {
        /* Ensure the operation is only performed on the server. */
        if (world.isClientSide()) { return false; }

        Identifier id = getDimensionTypeId(world);

        /* Ensure the dimension has a chair registry before adding the new chair. */
        CHAIRS.computeIfAbsent(id, ignored -> new HashMap<>()).put(blockPos, Pair.of(entity, playerPos));

        return true;
    }

    public static void removeChairEntity(Level world, BlockPos pos) {
        /* Ensure the operation is only performed on the server. */
        if (world.isClientSide()) { return; }

        Identifier id = getDimensionTypeId(world);
        Map<BlockPos, Pair<ChairEntity, BlockPos>> chairs = CHAIRS.get(id);

        /* Stop if there are no tracked chairs within this dimension. */
        if (chairs == null) { return; }

        chairs.remove(pos);
    }

    public static ChairEntity getChairEntity(Level world, BlockPos pos) {
        /* Ensure the lookup is only performed on the server. */
        if (world.isClientSide()) { return null; }

        Identifier id = getDimensionTypeId(world);
        Map<BlockPos, Pair<ChairEntity, BlockPos>> chairs = CHAIRS.get(id);

        /* Stop if there are no tracked chairs within this dimension. */
        if (chairs == null) { return null; }

        Pair<ChairEntity, BlockPos> chair = chairs.get(pos);

        /* Stop if there is no chair associated with this block position. */
        if (chair == null) { return null; }

        return chair.getFirst();
    }

    public static FriendlyByteBuf create() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    public static void popResourceFromFace(Level level, BlockPos pos, Direction side, ItemStack stack) {
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos);

        double itemWidth = EntityTypes.ITEM.getWidth();
        double itemHeight = EntityTypes.ITEM.getHeight();

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        /* Position the item against the requested face and offset it away from the block. */
        switch (side) {
            case DOWN -> y = pos.getY() - shape.min(Direction.Axis.Y) - itemHeight * 2.0;
            case UP -> y = pos.getY() + shape.max(Direction.Axis.Y);
            case NORTH -> z = pos.getZ() + shape.min(Direction.Axis.Z) - itemWidth;
            case SOUTH -> z = pos.getZ() + shape.max(Direction.Axis.Z) + itemWidth;
            case WEST -> x = pos.getX() + shape.min(Direction.Axis.X) - itemWidth;
            case EAST -> x = pos.getX() + shape.max(Direction.Axis.X) + itemWidth;
        }

        int stepX = side.getStepX();
        int stepY = side.getStepY();
        int stepZ = side.getStepZ();

        RandomSource random = level.getRandom();

        /* Push the item away from the selected face while adding slight randomness on the other axes. */
        double velocityX = stepX == 0 ? Mth.nextDouble(random, -0.1, 0.1) : stepX * 0.1;
        double velocityY = stepY == 0 ? Mth.nextDouble(random, 0.0, 0.1) : stepY * 0.1 + 0.1;
        double velocityZ = stepZ == 0 ? Mth.nextDouble(random, -0.1, 0.1) : stepZ * 0.1;

        ItemEntity item = new ItemEntity(level, x, y, z, stack, velocityX, velocityY, velocityZ);

        popResource(level, item, stack);
    }

    private static void popResource(Level level, ItemEntity itemEntity, ItemStack itemStack) {
        /* Only spawn dropped resources on the server. */
        if (level.isClientSide()) { return; }

        /* Ignore empty item stacks. */
        if (itemStack.isEmpty()) { return; }

        /* Ensure the level is a server level before checking server game rules. */
        if (!(level instanceof ServerLevel serverLevel)) { return; }

        /* Respect the block drops game rule before spawning the item entity. */
        if (!serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)) { return; }

        itemEntity.setDefaultPickUpDelay();

        level.addFreshEntity(itemEntity);
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        int rotations = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        VoxelShape rotatedShape = shape;

        /* Rotate the shape until it matches the target direction. */
        for (int rotation = 0; rotation < rotations; rotation++) {
            rotatedShape = rotateShapeClockwise(rotatedShape);
        }

        return rotatedShape;
    }

    private static VoxelShape rotateShapeClockwise(VoxelShape shape) {
        VoxelShape[] result = {Shapes.empty()};

        /* Rotate every box within the shape 90 degrees clockwise around the Y axis. */
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
            result[0] = Shapes.joinUnoptimized(result[0], Shapes.box(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX), BooleanOp.OR)
        );

        return result[0];
    }

    public static Optional<Pair<Float, Float>> getRelativeHitCoordinatesForBlockFace(BlockHitResult hit, Direction direction, Direction[] disallowedDirections) {
        Direction hitDirection = hit.getDirection();

        /* Reject interactions against explicitly disallowed block faces. */
        for (Direction disallowedDirection : disallowedDirections) {
            if (disallowedDirection == hitDirection) { return Optional.empty(); }
        }

        /* Only accept the requested horizontal face or either vertical face. */
        if (hitDirection != direction && hitDirection != Direction.UP && hitDirection != Direction.DOWN) {
            return Optional.empty();
        }

        BlockPos adjacentPos = hit.getBlockPos().relative(hitDirection);
        Vec3 hitLocation = hit.getLocation().subtract(adjacentPos.getX(), adjacentPos.getY(), adjacentPos.getZ());

        float x = (float) hitLocation.x();
        float y = (float) hitLocation.y();
        float z = (float) hitLocation.z();

        /* Use the requested facing direction when the top or bottom of the block was targeted. */
        Direction effectiveDirection = hitDirection == Direction.UP || hitDirection == Direction.DOWN ? direction : hitDirection;

        /* Convert the hit position into coordinates relative to the effective block face. */
        return switch (effectiveDirection) {
            case NORTH -> Optional.of(Pair.of(1.0F - x, y));
            case SOUTH -> Optional.of(Pair.of(x, y));
            case WEST -> Optional.of(Pair.of(z, y));
            case EAST -> Optional.of(Pair.of(1.0F - z, y));
            default -> Optional.empty();
        };
    }

//    public static NonNullList<Ingredient> deserializeIngredients(JsonArray json) {
//        NonNullList<Ingredient> ingredients = NonNullList.create();
//
//        for(int i = 0; i < json.size(); ++i) {
//            Ingredient ingredient = Ingredient.(json.get(i));
//            if (!ingredient.isEmpty()) {
//                ingredients.add(ingredient);
//            }
//        }
//
//        return ingredients;
//    }

    public static ItemStack convertStackAfterFinishUsing(LivingEntity entity, ItemStack used, Item returnItem, Item usedItem) {
        /* Trigger consumption criteria and usage statistics for server-side players. */
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, used);

            serverPlayer.awardStat(Stats.ITEM_USED.get(usedItem));
        }

        /* Return the replacement item directly once the consumed stack is empty. */
        if (used.isEmpty()) {
            return new ItemStack(returnItem);
        }

        /* Give the replacement item back to non-creative players. */
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack returnStack = new ItemStack(returnItem);

            /* Drop the replacement item if the player's inventory is full. */
            if (!player.getInventory().add(returnStack)) {
                player.drop(returnStack, false);
            }
        }

        return used;
    }

    public enum LineConnectingType implements StringRepresentable {
        NONE("none"),
        MIDDLE("middle"),
        LEFT("left"),
        RIGHT("right");

        private final String serializedName;

        LineConnectingType(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return serializedName;
        }
    }
}
