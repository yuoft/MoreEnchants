package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;

public class FastBow extends ModEnchantBase {

    public FastBow(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 5;
    }
}
