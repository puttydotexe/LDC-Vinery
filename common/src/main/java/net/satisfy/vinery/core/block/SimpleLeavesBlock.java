package net.satisfy.vinery.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;

public class SimpleLeavesBlock extends LeavesBlock {
    public static final MapCodec<SimpleLeavesBlock> CODEC = simpleCodec(SimpleLeavesBlock::new);

    public SimpleLeavesBlock(Properties properties) {
        super(0.01F, properties);
    }

    @Override
    public MapCodec<SimpleLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
    }
}
