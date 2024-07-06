package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class FastBow extends ModEnchantBase {

    public FastBow(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + enchantmentLevel * 5;
    }
}
