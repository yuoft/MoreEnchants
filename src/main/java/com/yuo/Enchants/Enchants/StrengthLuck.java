package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class StrengthLuck extends ModEnchantBase {
    protected StrengthLuck(Rarity rarityIn, EnchantType type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 21 + enchantmentLevel * 9;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.BLOCK_FORTUNE && ench != EnchantRegistry.rangBreak.get()
                && ench != EnchantRegistry.unLuck.get() && ench != EnchantRegistry.melting.get();
    }

    /**
     * 强运方块额外掉落
     *
     * @param block  挖掘方块
     * @param state  方块
     * @param world  世界
     * @param pos    坐标
     * @param player 玩家
     */
    public static void strengthLuck(Block block, BlockState state, Level world, BlockPos pos, int strengthLuck, Player player) {
        if (player.isCreative()) return;
        //矿物类方块 树叶 草类 海草 花
        if (block instanceof OreBlock || block instanceof CropBlock || block instanceof LeavesBlock || block instanceof SweetBerryBushBlock
                || block == Blocks.GLOWSTONE || block == Blocks.MELON || block == Blocks.SNOW || block == Blocks.CLAY || block == Blocks.BOOKSHELF) {
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) world, pos, null);
            if (drops.size() > 0)
                drops.forEach(drop -> {
                    int max = strengthLuck * 2 - 1;
                    if (world.random.nextGaussian() < 0.05f)
                        max += 3;
                    drop.setCount(world.random.nextInt(drop.getCount(), drop.getCount() * max));
                    world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                });
        } else if (block instanceof GlassBlock || block instanceof StainedGlassPaneBlock || block instanceof IceBlock) { //玻璃
            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(block)));
        } else {
            if (world.random.nextDouble() < 0.05 + 0.05 * strengthLuck) {
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) world, pos, null);
                if (drops.size() > 0)
                    drops.forEach(drop -> {
                        drop.setCount(drop.getCount() * 2);
                        world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                    });
            }
        }
    }

}
