package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

public class BlastArrow extends Enchantment {

    public BlastArrow(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
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
    //可附魔在什么物品上
    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof BowItem || item instanceof CrossbowItem) && stack.isEnchantable();
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //火矢
        return super.canApplyTogether(ench) && ench != Enchantments.FLAME;
    }
}
