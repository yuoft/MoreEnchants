package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class DiamondDrop extends ModEnchantBase{
    protected DiamondDrop(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 5;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return ench != this && ench != Enchantments.SILK_TOUCH; //精准
    }

    //钻石掉落
    public static void diamondDrop(int level, World world, int fortune, BlockPos pos){
        Random rand = world.rand;
        if (rand.nextDouble() < 0.05 + level * 0.1 + fortune * 0.1){
            ItemStack diamond = new ItemStack(Items.DIAMOND, MathHelper.nextInt(rand, 1, level + 3));
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), diamond));
        }
    }
}
