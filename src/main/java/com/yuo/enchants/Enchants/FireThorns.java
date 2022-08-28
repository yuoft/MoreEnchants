package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.Map;
import java.util.Random;

public class FireThorns extends ModEnchantBase{
    protected FireThorns(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.THORNS && ench != EnchantRegistry.superThorns.get();
    }

    @Override
    public void onUserHurt(LivingEntity user, Entity attacker, int level) {
        Random random = user.getRNG();
        Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(EnchantRegistry.fireThorns.get(), user);
        if (shouldHit(level, random)) {
            attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), getDamage(level, random));
            attacker.setFire(level);
            if (entry != null) {
                entry.getValue().damageItem(2, user, (livingEntity) -> {
                    livingEntity.sendBreakAnimation(entry.getKey());
                });
            }
        }

    }

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
