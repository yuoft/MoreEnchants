package com.yuo.Enchants.Enchants.Mc;

import com.yuo.Enchants.Config;
import com.yuo.Enchants.Enchants.ModEnchantBase;
import com.yuo.Enchants.Enchants.YEEnchants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;
import java.util.Random;

public class FireThorns extends ModEnchantBase {
    public FireThorns(Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 15 + enchantmentLevel * 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.THORNS && ench != YEEnchants.superThorns.get() &&
                ench != YEEnchants.thorns.get();
    }

    @Override
    public void doPostHurt(LivingEntity user, Entity attacker, int level) {
        if (!(attacker instanceof LivingEntity)) return;

        Random random = new Random();
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(YEEnchants.fireThorns.get(), user);
        if (shouldHit(level, random) && Config.SERVER.isFireThorns.get()) {
            attacker.hurt(user.damageSources().thorns(user), getDamage(level, random));
            attacker.setSecondsOnFire(level);
            if (entry != null) {
                entry.getValue().hurtAndBreak(2, user, e -> e.broadcastBreakEvent(entry.getKey()));
            }
        }
    }

    /**
     * 是否应该反伤攻击者
     * @param level 附魔等级
     * @param rnd 随机函数
     * @return 是否反伤
     */
    public static boolean shouldHit(int level, Random rnd) {
        if (level <= 0) {
            return false;
        } else {
            return rnd.nextFloat() < 0.15F * (float)level;
        }
    }

    public static float getDamage(int level, Random rnd) {
        return level > 10 ? level - 10 : 1 + rnd.nextInt(4);
    }
}
