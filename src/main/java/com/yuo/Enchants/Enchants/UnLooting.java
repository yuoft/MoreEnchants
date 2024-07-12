package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class UnLooting extends ModEnchantBase{
    protected UnLooting(Rarity rarityIn, EnchantType type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }


    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 10;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.MOB_LOOTING && ench != EnchantRegistry.robbery.get();
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
