package com.yuo.enchants.Enchants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class StrengthLuck extends ModEnchantBase{
    protected StrengthLuck(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.FORTUNE;
    }

    /**
     * 强运方块额外掉落
     * @param block 挖掘方块
     * @param state 方块
     * @param world 世界
     * @param pos 坐标
     */
    public static void strengthLuck(Block block, BlockState state, World world, BlockPos pos, int strengthLuck){
        List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
        if (drops.size() > 0)
            drops.forEach(drop -> {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                int max = strengthLuck * 2 - 1;
                if (world.rand.nextGaussian() < 0.05f)
                    max += 3;
                drop.setCount(MathHelper.nextInt(world.rand, drop.getCount(), drop.getCount() * max));
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
            });
//        else {
//            if (world.rand.nextGaussian() < 0.2 + strengthLuck * 0.1){ // 0.2 + lv * 0.1概率获取
//                ItemStack drop = new ItemStack(Item.BLOCK_TO_ITEM.getOrDefault(block, Items.AIR));
//                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
//            }
//        }
    }

}
