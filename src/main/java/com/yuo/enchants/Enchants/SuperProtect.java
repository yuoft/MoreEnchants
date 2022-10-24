package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class SuperProtect extends ModEnchantBase {

    private final int enchantType;

    public SuperProtect(Rarity rarityIn, int type, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
        this.enchantType = type;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //保护
        if (enchantType == 0)
            return this != ench && ench != Enchantments.PROTECTION;
        else if (enchantType == 1){
            return this != ench && ench != Enchantments.FIRE_PROTECTION;
        }else if (enchantType == 2){
            return this != ench && ench != Enchantments.FEATHER_FALLING;
        }else if (enchantType == 3){
            return this != ench && ench != Enchantments.BLAST_PROTECTION;
        }else return this != ench && ench != Enchantments.PROJECTILE_PROTECTION;
    }

    //额外保护
    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        if (source.canHarmInCreative()) {
            return 0;
        } else if (enchantType == 0) {
            return level * 2;
        } else if (enchantType == 1 && source.isFireDamage()) {
            return level * 3;
        } else if (enchantType == 2 && source == DamageSource.FALL) {
            return level * 4;
        } else if (enchantType == 3 && source.isExplosion()) {
            return level * 3;
        } else {
            return enchantType == 4 && source.isProjectile() ? level * 3 : 0;
        }
    }

    //减少火焰 和 爆炸伤害
    public static float getDamage(float damage, int enchant) {
        damage -= MathHelper.floor((float)damage * (float)enchant * 0.1F);
        return damage;
    }

}
