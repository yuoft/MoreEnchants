package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import java.util.Random;

public class UnDurable extends Enchantment {

    public UnDurable(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
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
                || item instanceof ShearsItem || item instanceof FishingRodItem) && stack.isEnchantable();
    }
    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //耐久
        return this != ench && ench != Enchantments.UNBREAKING;
    }
    //根据附魔等级 有60%概率额外消耗耐久对应耐久 盔甲
    public static boolean addDamage(ItemStack stack, int level, Random rand) {
        if (stack.getItem() instanceof ArmorItem && rand.nextFloat() < 0.6F) {
            return false;
        } else {
            return rand.nextInt(level + 1) > 0;
        }
    }
}
