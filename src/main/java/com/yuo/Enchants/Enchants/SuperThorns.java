package com.yuo.Enchants.Enchants;

import com.yuo.Enchants.Config;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;
import java.util.Random;

public class SuperThorns extends ModEnchantBase{
    protected SuperThorns(Rarity rarityIn, EnchantType type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.THORNS && ench != EnchantRegistry.thorns.get()
                && ench != EnchantRegistry.fireThorns.get();
    }

    @Override
    public void doPostHurt(LivingEntity user, Entity attacker, int level) {
        Random random = user.getRandom();
        //获取有此附魔的装备
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(EnchantRegistry.superThorns.get(), user);
        if (shouldHit(level, random) && Config.SERVER.isSuperThorns.get()) {
            attacker.hurt(DamageSource.thorns(user), getDamage(level, random));
            if (entry != null) {
                entry.getValue().hurtAndBreak(2, user, (livingEntity) -> {
                    livingEntity.broadcastBreakEvent(entry.getKey());
                });
            }
        }

    }

    public static boolean shouldHit(int level, Random rnd) {
        if (level <= 0) {
            return false;
        } else {
            return rnd.nextFloat() < 0.2F * (float)level;
        }
    }

    //反伤值 = lv + rand
    public static float getDamage(int level, Random rnd) {
        return level > 10 ? level - 10 : level + rnd.nextInt(6);
    }
}
