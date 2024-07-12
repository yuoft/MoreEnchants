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
            BlockState blockstate = YEBlocks.coolingLava.get().defaultBlockState().setValue(CoolingLava.AGE, Math.max(25 - level * 10, 0));
            float f = (float)Math.min(16, 2 + level);
            BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-f, -1.0D, -f), pos.offset(f, -1.0D, f))) {
                if (blockpos.closerToCenterThan(living.position(), f)) {
                    blockpos$mutable.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = worldIn.getBlockState(blockpos$mutable);
                    if (blockstate1.isAir()) {
                        BlockState blockstate2 = worldIn.getBlockState(blockpos);
                        boolean isFull = blockstate2.getBlock() == Blocks.LAVA && blockstate2.getValue(LiquidBlock.LEVEL) == 0; //是源头的岩浆方块
                        if (blockstate2.getMaterial() == Material.LAVA && isFull && blockstate.canSurvive(worldIn, blockpos)
                                && worldIn.isUnobstructed(blockstate, blockpos, CollisionContext.empty())
                                && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(living,
                                net.minecraftforge.common.util.BlockSnapshot.create(worldIn.dimension(), worldIn, blockpos),
                               Direction.UP)) {
                            worldIn.setBlockAndUpdate(blockpos, blockstate);
                            worldIn.scheduleTick(blockpos, YEBlocks.coolingLava.get(), Mth.nextInt(worldIn.getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
    }

}
