package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class Robbery extends ModEnchantBase {
    protected Robbery(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 21 + enchantmentLevel * 9;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && (ench != Enchantments.LOOTING || ench != EnchantRegistry.unLooting.get());
    }

    /**
     * 获取强化后抢夺等级
     * @param original 原等级
     * @param levelLooting 附魔等级
     * @return 新等级
     */
    public static int getLootingLevel(int original, int levelLooting) {
        int looting = original + levelLooting * 2; //每级强化抢夺等级 == 2级抢夺
        if (Math.random() < 0.05f)  //5%概率再加3抢夺等级
            looting += 3;

        return looting;
    }
}
