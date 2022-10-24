package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class ExpCorrode extends ModEnchantBase {

    public ExpCorrode(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    //负面附魔
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }
    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //经验修补
        return this != ench && ench != Enchantments.MENDING;
    }
}
