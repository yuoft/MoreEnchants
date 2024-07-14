package com.yuo.Enchants.Enchants;

import com.yuo.Enchants.Blocks.CoolingLava;
import com.yuo.Enchants.Blocks.YEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Iterator;

public class LavaWalker extends ModEnchantBase {

    public LavaWalker(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
    //是宝藏吗？
    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.FROST_WALKER;
    }

    //凝固岩浆
    public static void freezingNearby(LivingEntity living, Level worldIn, BlockPos pos, int level) {
        if (living.isOnGround()) {
            BlockState blockstate = YEBlocks.coolingLava.get().defaultBlockState().setValue(CoolingLava.AGE, Math.max(7 - level * 3, 0));
            float f = (float)Math.min(16, 2 + level);

            for (BlockPos next : BlockPos.betweenClosed(pos.offset(-f, 0, -f), pos.offset(f, 0, f))) {
                if (next.closerToCenterThan(living.position(), f) && !worldIn.getBlockState(next).isAir()) { //距离小于f  不是空气
                    BlockState blockstate2 = worldIn.getBlockState(next);
                    boolean isFull = blockstate2.getBlock() == Blocks.LAVA && blockstate2.getValue(LiquidBlock.LEVEL) == 0; //是源头的岩浆方块
                    if (blockstate2.getMaterial() == Material.LAVA && isFull && blockstate.canSurvive(worldIn, next)
                            && worldIn.isUnobstructed(blockstate, next, CollisionContext.empty())
                            && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(living,
                            net.minecraftforge.common.util.BlockSnapshot.create(worldIn.dimension(), worldIn, next),
                            Direction.UP)) {
                        worldIn.setBlockAndUpdate(next, blockstate);
                    }
                }
            }
        }
    }

}
