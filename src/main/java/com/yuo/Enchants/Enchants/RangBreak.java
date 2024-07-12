package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;

public class RangBreak extends ModEnchantBase {

    public RangBreak(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 15 + pLevel * 5;
    }

}
