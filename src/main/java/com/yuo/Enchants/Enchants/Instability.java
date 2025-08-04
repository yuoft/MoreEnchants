package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;

public class Instability extends ModEnchantBase{
    protected Instability(Rarity rarityIn, EnchantType type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 15 + enchantmentLevel * 6;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

}
