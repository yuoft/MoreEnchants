package com.yuo.Enchants.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CoolingLava extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    public CoolingLava() {
        super(Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).randomTicks().strength(1.0F, 2.2F).sound(SoundType.BASALT));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 7));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        int age = state.getValue(AGE);
        if (age >= 7){
            level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
        }else {
            level.setBlockAndUpdate(pos, state.setValue(AGE, Math.min(7, age + Mth.nextInt(random, 3, 6))));
        }
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
        pLevel.setBlockAndUpdate(pPos, Blocks.LAVA.defaultBlockState()); //破坏后变为岩浆
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