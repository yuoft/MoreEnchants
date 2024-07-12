package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;

public class Health extends ModEnchantBase {

    public Health(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 10 + pLevel * 5;
    }

}
