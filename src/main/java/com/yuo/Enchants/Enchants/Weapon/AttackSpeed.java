package com.yuo.Enchants.Enchants.Weapon;

import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class AttackSpeed extends ModEnchantBase {
    public AttackSpeed(Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 17 + enchantmentLevel * 3;
    }
}
