package fr.universecorp.mysticaluniverse.client.screens.renderer;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface ColoredBlock {
    int getColor(int tint);
    int getColor(BlockState state, BlockPos pos, int tintIndex);
}
