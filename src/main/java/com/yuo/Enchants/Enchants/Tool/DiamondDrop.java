package com.yuo.Enchants.Enchants.Tool;

import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class DiamondDrop extends ModEnchantBase {
    public DiamondDrop(Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 10 + pLevel * 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return ench != this && ench != Enchantments.SILK_TOUCH; //精准
    }

    //钻石掉落
    public static void diamondDrop(int level, Level world, int fortune, BlockPos pos){
        RandomSource rand = world.random;
        if (rand.nextDouble() < 0.05 + level * 0.05 + fortune * 0.1){
            ItemStack diamond = new ItemStack(Items.DIAMOND, world.random.nextInt(1, level + 3));
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), diamond));
        }
    }
}
