package com.yuo.Enchants.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Random;

public class CoolingLava extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_25;

    public CoolingLava() {
        super(Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).randomTicks().strength(1.0F, 2.2F).sound(SoundType.BASALT));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        super.randomTick(state, level, pos, random);
        int age = state.getValue(AGE);
        if (age >= 25){
            level.setBlock(pos, Blocks.LAVA.defaultBlockState(), 11);
        }else {
            level.setBlock(pos, state.setValue(AGE, Math.min(25, age + random.nextInt(3))), 11);
        }
    }

    //获取中键取到的方块对应物品
    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return ItemStack.EMPTY;
    }

    //添加属性
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

}
