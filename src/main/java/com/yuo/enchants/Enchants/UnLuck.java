package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class UnLuck extends ModEnchantBase{
    protected UnLuck(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && (ench != Enchantments.FORTUNE || ench != EnchantRegistry.strengthLuck.get());
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
