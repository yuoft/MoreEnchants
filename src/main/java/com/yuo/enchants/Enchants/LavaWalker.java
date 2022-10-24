package com.yuo.enchants.Enchants;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;

public class LavaWalker extends ModEnchantBase {

    public LavaWalker(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
    //是宝藏吗？
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.FROST_WALKER;
    }
    //凝固岩浆
    public static void freezingNearby(LivingEntity living, World worldIn, BlockPos pos, int level) {
        if (living.isOnGround()) {
            BlockState blockstate = Blocks.OBSIDIAN.getDefaultState();
            float f = (float)Math.min(16, 1 + level);
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

            for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-f, -1.0D, -f), pos.add(f, -1.0D, f))) {
                if (blockpos.withinDistance(living.getPositionVec(), f)) {
                    blockpos$mutable.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = worldIn.getBlockState(blockpos$mutable);
                    if (blockstate1.isAir(worldIn, blockpos$mutable)) {
                        BlockState blockstate2 = worldIn.getBlockState(blockpos);
                        boolean isFull = blockstate2.getBlock() == Blocks.LAVA && blockstate2.get(FlowingFluidBlock.LEVEL) == 0; //是源头的岩浆方块
                        if (blockstate2.getMaterial() == Material.LAVA && isFull && blockstate.isValidPosition(worldIn, blockpos)
                                && worldIn.placedBlockCollides(blockstate, blockpos, ISelectionContext.dummy())
                                && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(living,
                                net.minecraftforge.common.util.BlockSnapshot.create(worldIn.getDimensionKey(), worldIn, blockpos),
                                net.minecraft.util.Direction.UP)) {
                            worldIn.setBlockState(blockpos, blockstate);
                            worldIn.getPendingBlockTicks().scheduleTick(blockpos, Blocks.BASALT, MathHelper.nextInt(living.getRNG(), 60, 120));
                        }
                    }
                }
            }

        }
    }

}
