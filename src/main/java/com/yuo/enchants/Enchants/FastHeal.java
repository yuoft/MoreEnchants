package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class FastHeal extends ModEnchantBase {

    public FastHeal(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 5;
    }

    //额外恢复值
    public static float fastHeal(int fastHeal, float amount){
        return amount + (float) Math.ceil(amount * fastHeal * 0.25);
    }
}
