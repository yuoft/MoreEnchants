package com.yuo.Enchants.Enchants.Bad;

import com.yuo.Enchants.Enchants.ModEnchantBase;
import com.yuo.Enchants.Enchants.YEEnchants;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class UnLooting extends ModEnchantBase {
    public UnLooting(Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }


    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 10;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.MOB_LOOTING && ench != YEEnchants.robbery.get();
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
