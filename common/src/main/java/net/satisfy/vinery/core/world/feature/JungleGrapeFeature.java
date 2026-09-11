package net.satisfy.vinery.core.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.satisfy.vinery.core.block.GrapeVineBlock;

public class JungleGrapeFeature extends Feature<BlockStateConfiguration> {

    private static final int PLACEMENT_ATTEMPTS = 12;
    private static final int HORIZONTAL_RANGE = 7;
    private static final int VERTICAL_RANGE = 10;
    private static final int MAX_VINE_LENGTH = 12;

    public JungleGrapeFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        BlockPos origin = context.origin();
        BlockState vineState = context.config().state;

        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos adjacentPosition = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos abovePosition = new BlockPos.MutableBlockPos();

        for (int attempt = 0; attempt < PLACEMENT_ATTEMPTS; attempt++) {
            position.set(
                origin.getX() + random.nextInt(HORIZONTAL_RANGE * 2 + 1) - HORIZONTAL_RANGE,
                origin.getY() + random.nextInt(VERTICAL_RANGE) - 1,
                origin.getZ() + random.nextInt(HORIZONTAL_RANGE * 2 + 1) - HORIZONTAL_RANGE
            );

            /* Skip this placement attempt if the randomly generated position is occupied. */
            if (!level.isEmptyBlock(position)) { continue; }

            int chunkX = position.getX() >> 4;
            int chunkZ = position.getZ() >> 4;
            int targetY = position.getY() - getVineLength(random);

            /* Continue extending the vine downward until the target Y coordinate is reached. */
            while (position.getY() >= targetY) {
                /* Stop extending the vine if the current position is occupied. */
                if (!level.isEmptyBlock(position)) { break; }

                abovePosition.setWithOffset(position, Direction.UP);

                BlockState aboveState = level.getBlockState(abovePosition);

                /* Check each horizontal direction for a valid block face to attach the vine to. */
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    adjacentPosition.setWithOffset(position, direction);

                    /* Skip directions that would cross into a neighbouring chunk. */
                    if ((adjacentPosition.getX() >> 4) != chunkX || (adjacentPosition.getZ() >> 4) != chunkZ) { continue; }

                    BlockState adjacentState = level.getBlockState(adjacentPosition);
                    BlockState candidateState = vineState.setValue(
                        GrapeVineBlock.getPropertyForFace(direction),
                        true
                    );

                    /* Ensure the vine has valid support and is not attached to moss carpet. */
                    if (!adjacentState.is(Blocks.MOSS_CARPET) && candidateState.canSurvive(level, position)) {
                        level.setBlock(
                            position,
                            candidateState
                                .setValue(VineBlock.UP, aboveState.canOcclude())
                                .setValue(GrapeVineBlock.AGE, random.nextInt(3)),
                            2
                        );
                        break;
                    }

                    /* Continue the vine downward if the block above is another grape vine. */
                    if (aboveState.is(vineState.getBlock())) {
                        level.setBlock(
                            position,
                            aboveState
                                .setValue(VineBlock.UP, false)
                                .setValue(GrapeVineBlock.AGE, random.nextInt(3)),
                            2
                        );
                        break;
                    }
                }

                /* Move down one block for the next vine segment. */
                position.move(Direction.DOWN);
            }
        }

        return true;
    }

    private static int getVineLength(RandomSource random) {
        return MAX_VINE_LENGTH - random.nextInt(random.nextInt(MAX_VINE_LENGTH) + 1);
    }
}
