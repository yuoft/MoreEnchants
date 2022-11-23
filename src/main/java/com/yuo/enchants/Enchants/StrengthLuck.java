package com.yuo.enchants.Enchants;

import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
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
        return this != ench && (ench != Enchantments.FORTUNE || ench != EnchantRegistry.rangBreak.get()
                || ench != EnchantRegistry.unLuck.get() || ench != EnchantRegistry.rangBreak.get());
    }

    /**
     * 强运方块额外掉落
     * @param block 挖掘方块
     * @param state 方块
     * @param world 世界
     * @param pos 坐标
     * @param stack 工具
     * @param player 玩家
     */
    public static void strengthLuck(Block block, BlockState state, World world, BlockPos pos, int strengthLuck, ItemStack stack, PlayerEntity player){
        if (player.isCreative()) return;
        //矿物类方块 树叶 草类 海草 花
        if (block instanceof OreBlock || block == Blocks.GLOWSTONE || block == Blocks.MELON || block == Blocks.SNOW
            || block == Blocks.CLAY ||block == Blocks.BOOKSHELF){
            List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
            if (drops.size() > 1 )
                drops.forEach(drop -> {
//                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    int max = strengthLuck * 2 - 1;
                    if (world.rand.nextGaussian() < 0.05f)
                        max += 3;
                    drop.setCount(MathHelper.nextInt(world.rand, drop.getCount(), drop.getCount() * max));
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                });
            else {
                drops.forEach(drop -> {
                    drop.setCount(drop.getCount() * 2);
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                });
            }
        } else if (block instanceof GlassBlock || block instanceof StainedGlassPaneBlock || block instanceof IceBlock){
            world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(block)));
        } else {
            ItemStack copy = stack.copy();
            HashMap<Enchantment, Integer> map = new HashMap<>();
            map.put(Enchantments.FORTUNE, 10);
            EnchantmentHelper.setEnchantments(map, copy);
            Block.spawnDrops(state, world, pos, null, player, copy);
        }
    }

}
