package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class Instability extends ModEnchantBase{
    protected Instability(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 15 + enchantmentLevel * 6;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

}
