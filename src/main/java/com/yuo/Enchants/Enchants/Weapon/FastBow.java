package com.yuo.Enchants.Enchants.Weapon;

import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FastBow extends ModEnchantBase {

    public FastBow(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 5;
    }
}
