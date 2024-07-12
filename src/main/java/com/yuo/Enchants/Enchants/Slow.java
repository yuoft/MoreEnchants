package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class Slow extends ModEnchantBase {

    public Slow(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 10 + pLevel * 6;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.BLOCK_EFFICIENCY;//效率
    }
}
