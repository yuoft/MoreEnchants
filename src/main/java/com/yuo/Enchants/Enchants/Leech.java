package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;

public class Leech extends ModEnchantBase {

    public Leech(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 1 + pLevel * 9;
    }
}
