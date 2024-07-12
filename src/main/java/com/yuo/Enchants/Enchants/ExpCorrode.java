package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class ExpCorrode extends ModEnchantBase {

    public ExpCorrode(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    //负面附魔
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 25;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        //经验修补
        return this != ench && ench != Enchantments.MENDING;
    }
}
