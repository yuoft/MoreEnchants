package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import java.util.Random;

public class LightningDamage extends Enchantment {

    public LightningDamage(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    //是宝藏吗？
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return true;
    }
}
