package com.yuo.enchants.Blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CoolingLava extends BreakableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_25;

    public CoolingLava() {
        super(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).tickRandomly().hardnessAndResistance(1.0F, 2.2F).sound(SoundType.BASALT));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age >= 25){
            worldIn.setBlockState(pos, Blocks.LAVA.getDefaultState(), 11);
        }else {
            worldIn.setBlockState(pos, state.with(AGE, Math.min(25, age + random.nextInt(3))), 11);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

}
