package com.yuo.Enchants.Enchants.Armor;

import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FastHeal extends ModEnchantBase {

    public FastHeal(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 10 + pLevel * 5;
    }

    //额外恢复值
    public static float fastHeal(int fastHeal, float amount){
        return amount + (float) Math.ceil(amount * fastHeal * 0.25);
    }
}
