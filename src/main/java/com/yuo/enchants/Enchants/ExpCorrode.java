package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

public class ExpCorrode extends Enchantment {

    public ExpCorrode(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    //负面附魔
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }
    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof SwordItem || item instanceof ToolItem || item instanceof ArmorItem || item instanceof BowItem
        || item instanceof CrossbowItem || item instanceof TridentItem || item instanceof ShieldItem || item instanceof HoeItem
        || item instanceof ShearsItem  || item instanceof FishingRodItem) && item.isEnchantable(stack);
    }
    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //经验修补
        return ench != Enchantments.MENDING;
    }
}
