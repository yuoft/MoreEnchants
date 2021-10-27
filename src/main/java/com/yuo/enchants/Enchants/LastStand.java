package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

public class LastStand extends Enchantment {

    public LastStand(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

//    @Override
//    public boolean canApply(ItemStack stack) {
//        EquipmentSlotType slot = stack.getEquipmentSlot();
//        return slot == EquipmentSlotType.FEET && stack.isEnchantable();
//    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return true;
    }
}
