package com.yuo.Enchants.Enchants.Mc;

import com.yuo.Enchants.Config;
import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class SuperProtect extends ModEnchantBase {

    private final int enchantType;

    public SuperProtect(Rarity rarityIn, int type, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.enchantType = type;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        //保护
        if (enchantType == 0)
            return this != ench && ench != Enchantments.ALL_DAMAGE_PROTECTION;
        else if (enchantType == 1){
            return this != ench && ench != Enchantments.FIRE_PROTECTION;
        }else if (enchantType == 2){
            return this != ench && ench != Enchantments.FALL_PROTECTION;
        }else if (enchantType == 3){
            return this != ench && ench != Enchantments.BLAST_PROTECTION;
        }else return this != ench && ench != Enchantments.PROJECTILE_PROTECTION;
    }

    //额外保护
    @Override
    public int getDamageProtection(int level, DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        } else if (enchantType == 0 && Config.SERVER.isSuperProtect.get()) {
            return level * 2;
        } else if (enchantType == 1 && source.is(DamageTypeTags.IS_FIRE) && Config.SERVER.isSuperFire.get()) {
            return level * 3;
        } else if (enchantType == 2 && source.is(DamageTypeTags.IS_FALL) && Config.SERVER.isSuperFall.get()) {
            return level * 4;
        } else if (enchantType == 3 && source.is(DamageTypeTags.IS_EXPLOSION) && Config.SERVER.isSuperBlast.get()) {
            return level * 3;
        } else {
            return enchantType == 4 && source.is(DamageTypeTags.IS_PROJECTILE) && Config.SERVER.isSuperArrow.get() ? level * 3 : 0;
        }
    }

    //减少火焰 和 爆炸伤害
    public static float getDamage(float damage, int enchant) {
        damage -= (float) Math.floor(damage * (float)enchant * 0.1F);
        return damage;
    }

}
