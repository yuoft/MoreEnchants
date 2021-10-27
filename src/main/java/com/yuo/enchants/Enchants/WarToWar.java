package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class WarToWar extends Enchantment {

    public WarToWar(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 9;
    }
    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof SwordItem || item instanceof AxeItem) && item.isEnchantable(stack);
    }
    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return true;
    }
}
