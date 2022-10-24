package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class Leech extends ModEnchantBase {

    public Leech(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
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

}
