package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class Health extends ModEnchantBase {

    public Health(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 5;
    }

}
