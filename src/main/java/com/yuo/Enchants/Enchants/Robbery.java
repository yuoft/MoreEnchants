package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class Robbery extends ModEnchantBase {
    protected Robbery(Rarity rarityIn, EnchantType type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 21 + pLevel * 9;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.MOB_LOOTING && ench != EnchantRegistry.unLooting.get();
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
