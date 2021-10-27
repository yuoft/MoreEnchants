package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

public class Insight extends Enchantment {

    public Insight(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof SwordItem || item instanceof BowItem || item instanceof CrossbowItem || item instanceof ToolItem ||
                item instanceof TridentItem || item instanceof HoeItem || item instanceof FishingRodItem) && item.isEnchantable(stack);
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return true;
    }
}
