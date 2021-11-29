package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import java.util.Random;

public class BadLuckOfTheSea extends Enchantment {

    public BadLuckOfTheSea(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof FishingRodItem && stack.isEnchantable();
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //海之眷顾
        return this != ench && ench != Enchantments.LUCK_OF_THE_SEA;
    }
}
