package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;

public class HandRange extends ModEnchantBase {

    public HandRange(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 19 + pLevel * 4;
    }

}
