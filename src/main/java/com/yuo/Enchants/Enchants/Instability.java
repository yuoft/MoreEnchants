package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Instability extends ModEnchantBase{
    protected Instability(Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
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
