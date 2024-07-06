package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class HandRange extends ModEnchantBase {

    public HandRange(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 19 + enchantmentLevel * 4;
    }

}
