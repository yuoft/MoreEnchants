package com.yuo.enchants.Enchants;

import net.minecraft.inventory.EquipmentSlotType;

public class FastBow extends ModEnchantBase {

    public FastBow(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //增加使用时间
    public static int fastDraw(int fastBow, int charge){
        return charge + (int) Math.ceil(charge * fastBow * 0.25);
    }
}
