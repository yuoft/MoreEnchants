package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class UnLooting extends ModEnchantBase{
    protected UnLooting(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }


    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + enchantmentLevel * 10;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.LOOTING && ench != EnchantRegistry.robbery.get();
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
