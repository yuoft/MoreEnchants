package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class FireImmune extends Enchantment {

    public FireImmune(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }
    //是宝藏吗？
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    //附魔冲突
    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.FIRE_PROTECTION; //火焰保护
    }
}
