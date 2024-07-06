package com.yuo.enchants.Enchants;

import com.yuo.enchants.Config;
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
        return 15 + enchantmentLevel * 5;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.THORNS && ench != EnchantRegistry.superThorns.get() &&
                ench != EnchantRegistry.thorns.get();
    }

    @Override
    public void onUserHurt(LivingEntity user, Entity attacker, int level) {
        Random random = user.getRNG();
        Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(EnchantRegistry.fireThorns.get(), user);
        if (shouldHit(level, random) && Config.SERVER.isFireThorns.get()) {
            attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), getDamage(level, random));
            attacker.setFire(level);
            if (entry != null) {
                entry.getValue().damageItem(2, user, (livingEntity) -> {
                    livingEntity.sendBreakAnimation(entry.getKey());
                });
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
